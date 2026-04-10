package ru.ibs.transform.xml.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.math.min

@Service
class ImmutableManager(
    private val keyVerifierService: KeyVerifierService,
    private val xmlInputSaverService: XmlInputSaverService,
    private val sentToAbsService: SentToAbsService,
    private val receivedFromAbsService: ReceivedFromAbsService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun workflow(xmlString: String, signatureBytes: ByteArray, certificateName: String): ImmuableManagerResult {
        val verified = keyVerifierService.verifyText(xmlString, signatureBytes, certificateName)
        if (!verified) {
            return ImmuableManagerResult(ImmutableProcessStatuses.NOT_VERIFIED, null)
        }
        val savedXmlDoc = xmlInputSaverService.saveToDb(xmlString, certificateName)

        val sentToAbs = sentToAbsService.findByInputDoc(savedXmlDoc)
        if (sentToAbs == null) {
            sentToAbsService.send(savedXmlDoc)
            return ImmuableManagerResult(ImmutableProcessStatuses.ACCEPTED, null) // только что создали, приняли в работу
        }

        val receivedFromAbs = receivedFromAbsService.findBySentToAbs(sentToAbs)
        if (receivedFromAbs != null) {
            return ImmuableManagerResult(ImmutableProcessStatuses.COMPLETED, receivedFromAbs.xmlAbsAnswer)
        }

        return ImmuableManagerResult(ImmutableProcessStatuses.ACCEPTED, null) // похоже отправили, но еще не получен ответ
    }

    fun answerReceived(xmlInputDocMultiPart: MultipartFile, answerMultiPart: MultipartFile) {
        val xmlInputDocument = String(xmlInputDocMultiPart.bytes, Charsets.UTF_8)
        val answerString = String(answerMultiPart.bytes, Charsets.UTF_8)
        log.info(
            "Answer '{}' received for input document '{}'",
            answerString.substring(0, min(50, answerString.length - 1)),
            xmlInputDocument.substring(0, min(50, xmlInputDocument.length - 1))
        )

    }

}
