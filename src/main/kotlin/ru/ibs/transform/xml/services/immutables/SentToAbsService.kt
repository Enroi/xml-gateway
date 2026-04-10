package ru.ibs.transform.xml.services.immutables

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.XmlInputDocument
import ru.ibs.transform.xml.repositories.immutables.SentToAbsRepository
import kotlin.jvm.javaClass
import ru.ibs.transform.xml.entities.immutables.SentToAbs
import ru.ibs.transform.xml.services.AbsEmulatorService
import java.time.Instant


@Service
class SentToAbsService(
    private val sentToAbsRepository: SentToAbsRepository,
    private val canonicalService: CanonicalService,
    private val hashService: HashService,
    private val absEmulatorService: AbsEmulatorService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun send(xmlInputDocument: XmlInputDocument) {
        val jsonString = convertToJsonString(xmlInputDocument)
        sentToAbsRepository.merge(
            jsonSentAbsJson = jsonString,
            xmlInputDocumentHash = hashService.hash(xmlInputDocument.xmlData),
            jsonSentAbsHash = hashService.hash(jsonString),
            createdAt = Instant.now(),
        )
        log.info("Sending to ABS: {}", xmlInputDocument)
        val saved = findByInputDoc(xmlInputDocument)
        absEmulatorService.emulateForSentToAbs(saved!!)
    }

    private fun convertToJsonString(xmlInputDocument: XmlInputDocument): String =
        canonicalService.canonicalizeJson(
            mapOf(
                "xml" to xmlInputDocument.xmlData,
                "abs" to "ABS 1"
            )
        )

    fun findByInputDoc(xmlInputDocument: XmlInputDocument): SentToAbs? {
        return sentToAbsRepository.findByXmlInputDocumentHash(hashService.hash(xmlInputDocument.xmlData))
    }
}
