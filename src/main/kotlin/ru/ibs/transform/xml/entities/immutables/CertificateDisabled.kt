package ru.ibs.transform.xml.entities.immutables

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity
data class CertificateDisabled (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(columnDefinition = "TEXT")
    val certificate: String,

    @Column(unique = true, nullable = false)
    val certificateHash: ByteArray,

    val createdAt: Instant = Instant.now(),

    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CertificateDisabled

        return certificateHash.contentEquals(other.certificateHash)
    }

    override fun hashCode(): Int {
        return certificateHash.contentHashCode()
    }

}
