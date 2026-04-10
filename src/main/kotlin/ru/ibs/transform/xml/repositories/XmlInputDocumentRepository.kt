package ru.ibs.transform.xml.repositories

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.XmlInputDocument
import java.time.Instant

@Service
interface XmlInputDocumentRepository: JpaRepository<XmlInputDocument, Long> {
    @Modifying
    @Transactional
    @Query(
        value = """
        MERGE INTO xml_input_document AS target
        USING (VALUES (
            cast(:hashHex as BINARY VARYING(255)),
            :xmlData,
            :certificateName,
            :createdAt
        )) AS source (xml_input_hash, xml_data, certificate_name, created_at)
        ON target.xml_input_hash = source.xml_input_hash
        WHEN NOT MATCHED THEN INSERT 
            (xml_input_hash, xml_data, certificate_name, created_at)
        VALUES (
            source.xml_input_hash, 
            source.xml_data, 
            source.certificate_name, 
            source.created_at
        )
    """,
        nativeQuery = true
    )
    fun mergeDocument(
        @Param("hashHex") hash: ByteArray,
        @Param("xmlData") xmlData: String,
        @Param("certificateName") certificateName: String,
        @Param("createdAt") createdAt: Instant = Instant.now(),
    )

    fun findByXmlInputHash(hash: ByteArray): XmlInputDocument
}
