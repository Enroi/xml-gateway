package ru.ibs.transform.xml.services

import jakarta.annotation.PostConstruct
import org.apache.xml.security.Init
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.XmlInputDocument
import ru.ibs.transform.xml.repositories.XmlInputDocumentRepository
import java.security.MessageDigest

@Service
class XmlInputSaverService(
    private val xmlInputDocumentRepository: XmlInputDocumentRepository,
    private val canonicalService: CanonicalService,
    private val sentToAbsService: SentToAbsService,
    private val hashService: HashService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        if (!Init.isInitialized()) Init.init()
    }

    fun saveToDb(xmlString: String) {
        canonicalService.canonicalizeXml(xmlString)
            .let {
                XmlInputDocument(
                    id = null,
                    xmlInputHash = hashService.hash(it),
                    xmlData = it,
                )
            }.let {
                xmlInputDocumentRepository.save(it)
            }.also {
                log.debug("Xml document with id: {} saved to DB", it.id)
                sentToAbsService.send(it)
            }
    }

}
