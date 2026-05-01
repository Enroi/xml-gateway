package ru.ibs.transform.xml.services.immutables

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.configs.ProcessEvent
import ru.ibs.transform.xml.controllers.ResourceRequestDTO
import ru.ibs.transform.xml.entities.immutables.ResourceRequestStatus
import ru.ibs.transform.xml.repositories.immutables.ResourceRequestDocumentsRepository

@Service
class ResourceRequestService(
    private val resourceRequestDocumentsRepository: ResourceRequestDocumentsRepository,
    private val hashService: HashService,
    private val jsonFormatter: JsonFormatter,
    private val publisher: ApplicationEventPublisher,
) {

    fun work(request: ResourceRequestDTO): ResourceRequestDTO? {
        val dataJsonString = jsonFormatter.format(request)
        val documentHash = hashService.hash(dataJsonString)
        resourceRequestDocumentsRepository.merge(
            jsonHash = documentHash,
            jsonData = dataJsonString,
            documentType = ResourceRequestStatus.INITIAL.name,
            parentDocumentHash = null,
            parentDocumentJSON = null,
            resourceName = request.resourceName,
            requestId = request.requestId,
        )
        publisher.publishEvent(ProcessEvent(request))
        return null
    }
}
