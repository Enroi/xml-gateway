package ru.ibs.transform.xml.services

import org.springframework.stereotype.Service

@Service
class ImmutableManager(
    private val keyVerifierService: KeyVerifierService,
    private val xmlInputSaverService: XmlInputSaverService,
    private val sentToAbsService: SentToAbsService,
) {
    fun start(xmlString: String, signatureBytes: ByteArray, certificateName: String): ImmutableProcessStatuses {
        val verified = keyVerifierService.verifyText(xmlString, signatureBytes, certificateName)
        if (!verified) {
            return ImmutableProcessStatuses.NOT_VERIFIED
        }
        val savedXmlDoc = xmlInputSaverService.saveToDb(xmlString, certificateName)
        sentToAbsService.send(savedXmlDoc)
        return ImmutableProcessStatuses.ACCEPTED
    }

}
/*
insert into xml_input_document (certificate_name , created_at , xml_data , xml_input_hash )
values ('name', '2026-01-01', 'aba', 'aba')

 */
