package ru.ibs.transform.xml.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.ibs.transform.xml.services.ImmutableManager

@RestController
@RequestMapping("/api/receive")
class ReceiverController(
    private val immutableManager: ImmutableManager,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun receive(
        @RequestParam xmlInputDocumented: MultipartFile,
        @RequestParam answer: MultipartFile,
    ) {
        log.info("Received xml input document $xmlInputDocumented, and response is $answer")
        immutableManager.answerReceived(xmlInputDocumented, answer)
    }
}
