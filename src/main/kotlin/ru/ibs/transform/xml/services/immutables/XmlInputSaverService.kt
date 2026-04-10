package ru.ibs.transform.xml.services.immutables

import jakarta.annotation.PostConstruct
import org.apache.xml.security.Init
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.XmlInputDocument
import ru.ibs.transform.xml.repositories.XmlInputDocumentRepository

@Service
class XmlInputSaverService(
    private val xmlInputDocumentRepository: XmlInputDocumentRepository,
    private val hashService: HashService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        if (!Init.isInitialized()) Init.init()
    }

    fun saveToDb(
        xmlString: String,
        certificateName: String,
    ): XmlInputDocument {
        val hash = hashService.hash(xmlString)
        xmlInputDocumentRepository.mergeDocument(
            hash = hash,
            xmlData = xmlString,
            certificateName = certificateName,
        )
        return xmlInputDocumentRepository.findByXmlInputHash(hash)
    }

}
