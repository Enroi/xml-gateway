package ru.ibs.transform.xml.repositories

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.ibs.transform.xml.entities.immutables.SentToAbs
import java.time.Instant

interface SentToAbsRepository: JpaRepository<SentToAbs, Long> {

    fun findByXmlInputDocumentHash(hash: ByteArray): SentToAbs?

    @Modifying
    @Transactional
    @Query(
        value = """
        MERGE INTO SENT_TO_ABS AS target
        USING (VALUES (
            cast(:jsonSentAbsHash as BINARY VARYING(255)),
            :jsonSentAbsJson,
            cast(:xmlInputDocumentHash as BINARY VARYING(255)),
            :createdAt
        )) AS source (json_sent_abs_hash, json_sent_abs_json, xml_input_document_hash, created_at)
        ON target.json_sent_abs_hash = source.json_sent_abs_hash
        WHEN NOT MATCHED THEN INSERT 
            (json_sent_abs_hash, json_sent_abs_json, xml_input_document_hash, created_at)
        VALUES (
            source.json_sent_abs_hash, 
            source.json_sent_abs_json, 
            source.xml_input_document_hash, 
            source.created_at
        )
    """,
        nativeQuery = true
    )
    fun merge(
        jsonSentAbsHash: ByteArray,
        jsonSentAbsJson: String,
        xmlInputDocumentHash: ByteArray,
        createdAt: Instant
    )

}
