package ru.ibs.transform.xml.services.immutables

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.configs.ProcessEvent
import ru.ibs.transform.xml.entities.immutables.ResourceRequestDocument
import ru.ibs.transform.xml.entities.immutables.ResourceRequestStatus
import ru.ibs.transform.xml.repositories.immutables.ResourceRequestDocumentsRepository
import kotlin.random.Random

@Service
class ResourceRequestServiceAsync(
    private val resourceRequestDocumentsRepository: ResourceRequestDocumentsRepository,
    private val hashService: HashService,
    private val jsonFormatter: JsonFormatter,
    private val publisher: ApplicationEventPublisher,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    @Async
    fun doWorkWithDocuments(event: ProcessEvent) {
        log.info("Получен документ для обработки {}", event.source)

        resourceRequestDocumentsRepository.findForWork()
            .forEach { document ->
                when (document.documentType) {
                    ResourceRequestStatus.INITIAL -> assignNewStatus(
                        document,
                        ResourceRequestStatus.REJECTION_AFTER_FIRST,
                        ResourceRequestStatus.APPROVAL_AFTER_FIRST,
                    )

                    ResourceRequestStatus.APPROVAL_AFTER_FIRST -> assignNewStatus(
                        document,
                        ResourceRequestStatus.REJECTION_AFTER_SECOND,
                        ResourceRequestStatus.APPROVAL_AFTER_SECOND,
                    )

                    ResourceRequestStatus.APPROVAL_AFTER_SECOND -> assignNewStatus(
                        document,
                        ResourceRequestStatus.RESOURCES_NOT_FOUND,
                        ResourceRequestStatus.RESOURCES_FOUND,
                    )

                    ResourceRequestStatus.RESOURCES_FOUND -> assignNewStatus(
                        document,
                        ResourceRequestStatus.RESOURCES_READY_FOR_ISSUANCE,
                        ResourceRequestStatus.RESOURCES_READY_FOR_ISSUANCE,
                    )

                    ResourceRequestStatus.RESOURCES_READY_FOR_ISSUANCE -> assignNewStatus(
                        document,
                        ResourceRequestStatus.RESOURCES_ISSUED,
                        ResourceRequestStatus.RESOURCES_ISSUED,
                    )

                    // Блок завершения процесса
                    ResourceRequestStatus.REJECTION_AFTER_SECOND,
                    ResourceRequestStatus.REJECTION_AFTER_FIRST,
                    ResourceRequestStatus.RESOURCES_NOT_FOUND,
                    ResourceRequestStatus.RESOURCES_ISSUED -> assignNewStatus(
                        document,
                        ResourceRequestStatus.FINISHED,
                        ResourceRequestStatus.FINISHED,
                    )

                    ResourceRequestStatus.FINISHED -> Unit // Эти документы исключаются из обработки, но даже если они сюда попадут, они будут проигнорированы
                }
            }
    }

    private fun assignNewStatus(
        document: ResourceRequestDocument,
        lowProbability: ResourceRequestStatus,
        bigProbability: ResourceRequestStatus,
    ) {
        val oldDocumentJsonString = jsonFormatter.format(document.toDto())
        // С вероятностью в 10% выбирается маловероятный статус, остальные 90% выбирается более вероятный
        val randomValue = Random.nextInt(1, 11)
        val documentType = if (randomValue == 1) lowProbability else bigProbability
        val newDocument = document.copy(
            documentType = documentType,
        )
        val newDocumentJsonString = jsonFormatter.format(newDocument.toDto())
        resourceRequestDocumentsRepository.merge(
            jsonHash = hashService.hash(newDocumentJsonString),
            jsonData = newDocumentJsonString,
            documentType = newDocument.documentType.name,
            parentDocumentHash = hashService.hash(oldDocumentJsonString),
            parentDocumentJSON = oldDocumentJsonString,
            requestId = newDocument.requestId,
            resourceName = newDocument.resourceName,
        )
        if (bigProbability != ResourceRequestStatus.FINISHED) {
            publisher.publishEvent(ProcessEvent(newDocument.toDto()))
        }
    }
}
