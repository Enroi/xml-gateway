package ru.ibs.transform.xml.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.XmlInputDocument
import ru.ibs.transform.xml.repositories.SentToAbsRepository
import kotlin.jvm.javaClass
import ru.ibs.transform.xml.entities.immutables.SentToAbs


@Service
class SentToAbsService(
    private val sentToAbsRepository: SentToAbsRepository,
    private val canonicalService: CanonicalService,
    private val hashService: HashService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun send(xmlInputDocument: XmlInputDocument) {
        val sentMap = mutableMapOf<String, String>()
        sentMap["xml"] = xmlInputDocument.xmlData
        sentMap["abs"] = "ABS 1"

        val jsonString = canonicalService.canonicalizeJson(sentMap)
        val sentToAbs = SentToAbs(
            id = null,
            jsonSentAbsHash = hashService.hash(jsonString),
            jsonSentAbsJson = jsonString,
            xmlInputDocument = xmlInputDocument,
        )
        sentToAbsRepository.save(sentToAbs)

        log.info("Sending to ABS: {}", xmlInputDocument)

    }
}
