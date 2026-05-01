package ru.ibs.transform.xml.services.immutables

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.ResourceRequestDocument
import ru.ibs.transform.xml.entities.immutables.ResourceRequestStatus
import ru.ibs.transform.xml.repositories.immutables.ResourceRequestDocumentsRepository

@Service
class ResourceRequestServiceAsync(
    private val resourceRequestDocumentsRepository: ResourceRequestDocumentsRepository,
    private val hashService: HashService,
    private val jsonFormatter: JsonFormatter,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 5000)
    fun doWorkWithDocuments() {
        val documentsForWork = resourceRequestDocumentsRepository.findForWork()
        documentsForWork.forEach { document ->
            when(document.documentType) {
                ResourceRequestStatus.INITIAL -> workWithInitial(document)
                else -> throw RuntimeException("Тип документа ${document.documentType} не поддерживается")
            }
        }
    }

    private fun workWithInitial(document: ResourceRequestDocument) {
        log.info("Найден документ для обработки: {}", document)
        val oldDocumentJsonString = jsonFormatter.format(document.toDto())
        val oldDocumentHash = hashService.hash(oldDocumentJsonString)
        val newDocument = document.copy(
            documentType = ResourceRequestStatus.REJECTION_AFTER_FIRST,
//            resourceName = document.resourceName,
        )
        val newDocumentJsonString = jsonFormatter.format(newDocument.toDto())
        val newDocumentHash = hashService.hash(newDocumentJsonString)
        resourceRequestDocumentsRepository.merge(
            jsonHash = newDocumentHash,
            jsonData = newDocumentJsonString,
            documentType = newDocument.documentType.name,
            parentDocumentHash = oldDocumentHash,
            parentDocumentJSON = oldDocumentJsonString,
            requestTimeGeneration = newDocument.requestTimeGeneration,
            resourceName = newDocument.resourceName,
        )
    }
}
