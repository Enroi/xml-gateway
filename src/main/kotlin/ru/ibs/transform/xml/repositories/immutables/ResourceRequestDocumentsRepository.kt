package ru.ibs.transform.xml.repositories.immutables

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.ibs.transform.xml.entities.immutables.ResourceRequestDocument
import ru.ibs.transform.xml.entities.immutables.ResourceRequestStatus
import ru.ibs.transform.xml.entities.immutables.ResourceRequestStatus.FINISHED
import java.time.Instant

interface ResourceRequestDocumentsRepository : JpaRepository<ResourceRequestDocument, Long> {

    @Modifying
    @Transactional
    @Query(
        value = """
        MERGE INTO RESOURCE_REQUEST_DOCUMENT  AS target
        USING (VALUES (
            cast(:jsonHash as BINARY VARYING(255)),
            :jsonData,
            :createdAt,
            :documentType,
            cast(:parentDocumentHash as BINARY VARYING(255)),
            :parentDocumentJson,
            :resourceName,
            :requestId
        )) AS source (json_hash, JSON_DATA , created_at, DOCUMENT_TYPE, PARENT_DOCUMENT_HASH, PARENT_DOCUMENT_JSON, RESOURCE_NAME, REQUEST_ID)
        ON target.json_hash = source.json_hash
        WHEN NOT MATCHED THEN INSERT 
            (json_hash, JSON_DATA , created_at, DOCUMENT_TYPE, PARENT_DOCUMENT_HASH, PARENT_DOCUMENT_JSON, RESOURCE_NAME, REQUEST_ID)
        VALUES(
            source.json_hash, 
            source.JSON_DATA , 
            source.created_at, 
            source.DOCUMENT_TYPE, 
            source.PARENT_DOCUMENT_HASH,
            source.PARENT_DOCUMENT_JSON,
            source.RESOURCE_NAME,
            source.REQUEST_ID
        )
    """,
        nativeQuery = true
    )
    fun merge(
        @Param("jsonHash") jsonHash: ByteArray,
        @Param("jsonData") jsonData: String,
        @Param("documentType") documentType: String,
        @Param("parentDocumentHash") parentDocumentHash: ByteArray?,
        @Param("parentDocumentJson") parentDocumentJSON: String?,
        @Param("resourceName") resourceName: String,
        @Param("requestId") requestId: String,
        @Param("createdAt") createdAt: Instant = Instant.now(),
    )

    @Query(
        value = """
        select p.* from resource_request_document p
        where not exists (
            select 1 from resource_request_document c where c.parent_document_hash = p.json_hash
        )
        and document_type != 'FINISHED'
        and request_id = :requestId
        """,
        nativeQuery = true
    )
    fun findForWork(
        @Param("requestId") requestId: String
    ): List<ResourceRequestDocument>
}
