package ru.ibs.transform.xml.controllers

import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
class MessageController {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun process(@RequestBody messageXml: String): String {
        log.info("Message received")
        return ""
    }
}
