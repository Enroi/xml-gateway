package ru.ibs.transform.xml.controllers

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ibs.transform.xml.services.XmlInputSaverService
import kotlin.math.min

@RestController
@RequestMapping("/api/message")
class MessageController(
    val xmlInputSaverService: XmlInputSaverService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun process(
        @RequestBody messageXml: String,
        @RequestHeader("Certificate-name") certificateName: String,
        @RequestHeader("Message-hash") messageHash: ByteArray,
    ): String {
        val strForLog = messageXml.substring(0, min(50, messageXml.length - 1))
        val logMessageEnding = "certificate: $certificateName text:$strForLog"
        log.info("Message received {}", logMessageEnding)
        xmlInputSaverService.saveToDb(xmlString = messageXml)
        log.info("Message processed: {}", logMessageEnding)
        return ""
    }
}
