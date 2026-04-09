package ru.ibs.transform.xml.controllers

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ibs.transform.xml.services.ImmutableManager
import ru.ibs.transform.xml.services.ImmutableProcessStatuses
import ru.ibs.transform.xml.services.XmlInputSaverService
import kotlin.io.encoding.Base64
import kotlin.math.min

@RestController
@RequestMapping("/api/message")
class MessageController(
    val immutableManager: ImmutableManager,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun process(
        @RequestBody messageXml: String,
        @RequestHeader("Certificate-name") certificateName: String,
        @RequestHeader("Message-hash") signatureBase64: String,
    ): ImmutableProcessStatuses {
        val strForLog = messageXml.substring(0, min(50, messageXml.length - 1))
        val signatureBytes = Base64.decode(signatureBase64)
        val logMessageEnding = "certificate: $certificateName text:$strForLog"
        log.info("Message received {}", logMessageEnding)
        val result = immutableManager.start(
            xmlString = messageXml,
            signatureBytes = signatureBytes,
            certificateName = certificateName
        )
        log.info("Message processed: {}", logMessageEnding)
        return result
    }
}
