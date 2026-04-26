package ru.ibs.transform.xml.repositories.immutables

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.ibs.transform.xml.entities.immutables.CertificateEnabled
import java.time.Instant

interface CertificateEnabledRepository: JpaRepository<CertificateEnabled, Long> {

    @Modifying
    @Transactional
    @Query(
        value = """
        MERGE INTO certificate_enabled AS target
        USING (VALUES (
            cast(:certificateHash as BINARY VARYING(255)),
            :certificate,
            :createdAt
        )) AS source (certificate_hash, certificate, created_at)
        ON target.certificate_hash = source.certificate_hash
        WHEN NOT MATCHED THEN INSERT 
            (certificate_hash, certificate, created_at)
        VALUES (
            source.certificate_hash, 
            source.certificate, 
            source.created_at
        )
    """,
        nativeQuery = true
    )
    fun merge(
        certificateHash: ByteArray,
        certificate: String,
        createdAt: Instant = Instant.now(),
    )

    fun findByCertificateHash(certificateHash: ByteArray): CertificateEnabled?

}
