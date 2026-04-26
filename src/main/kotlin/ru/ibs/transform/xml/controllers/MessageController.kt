package ru.ibs.transform.xml.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ibs.transform.xml.services.immutables.ImmutableManager
import ru.ibs.transform.xml.services.immutables.ImmutableProcessStatuses
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
        @RequestHeader("Certificate") certificateBase64: String,
        @RequestHeader("Message-hash") signatureBase64: String,
    ): ResponseEntity<String> {
        val strForLog = messageXml.substring(0, min(50, messageXml.length - 1))
        val signatureBytes = Base64.decode(signatureBase64)
        val certificate = Base64.decode(certificateBase64).decodeToString()
        val logMessageEnding = "certificate: $certificate text:$strForLog"
        log.info("Message received {}", logMessageEnding)
        val result = immutableManager.workflow(
            xmlString = messageXml,
            signatureBytes = signatureBytes,
            certificate = certificate
        )
        val httpResult = when (result.status) {
            ImmutableProcessStatuses.COMPLETED -> ResponseEntity.ok(result.xmlResult)
            ImmutableProcessStatuses.ACCEPTED -> ResponseEntity.status(202).build()
            ImmutableProcessStatuses.NOT_VERIFIED -> ResponseEntity.status(401).build()
        }

        log.info("Message processed: {}", logMessageEnding)
        return httpResult
    }
}
