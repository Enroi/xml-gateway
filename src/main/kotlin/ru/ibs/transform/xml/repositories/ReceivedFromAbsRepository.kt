package ru.ibs.transform.xml.repositories

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.ibs.transform.xml.entities.immutables.ReceivedFromAbs
import java.time.Instant

@Repository
interface ReceivedFromAbsRepository: JpaRepository<ReceivedFromAbs, Long> {

    @Modifying
    @Transactional
    @Query(
        value = """
        MERGE INTO received_from_abs AS target
        USING (VALUES (
            cast(:jsonSentAbsHash as BINARY VARYING(255)),
            :jsonSentAbsJson,
            cast(:xmlAbsAnswerHash as BINARY VARYING(255)),
            :xmlAbsAnswer,
            :createdAt
        )) AS source (json_sent_abs_hash, json_sent_abs_json, xml_abs_answer_hash, xml_abs_answer, created_at)
        ON target.json_sent_abs_hash = source.json_sent_abs_hash
        WHEN NOT MATCHED THEN INSERT 
            (json_sent_abs_hash, json_sent_abs_json, xml_abs_answer_hash, created_at)
        VALUES (
            source.json_sent_abs_hash, 
            source.json_sent_abs_json, 
            source.xml_abs_answer_hash,
            source.xml_abs_answer,
            source.created_at
        )
    """,
        nativeQuery = true
    )
    fun merge(
        jsonSentAbsHash: ByteArray,
        jsonSentAbsJson: String,
        xmlAbsAnswer: String,
        xmlAbsAnswerHash: ByteArray,
        createdAt: Instant
    )

    fun findByJsonSentAbsHash(jsonSentAbsHash: ByteArray): ReceivedFromAbs?

}
