package ru.ibs.transform.xml.controllers

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ibs.transform.xml.services.immutables.ResourceRequestService

@RestController
@RequestMapping("/api/result")
class ResourceRequestController(
    private val resourceRequestService: ResourceRequestService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("work")
    fun work(@RequestBody request: ResourceRequestDTO): ResourceRequestDTO? {
        log.info("Received request: {}", request)
        return resourceRequestService.work(request)
    }
}
