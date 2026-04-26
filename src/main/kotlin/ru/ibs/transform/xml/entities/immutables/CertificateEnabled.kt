package ru.ibs.transform.xml.entities.immutables

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant
import kotlin.text.trimIndent

@Entity
data class CertificateEnabled(

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

        other as CertificateEnabled

        return certificateHash.contentEquals(other.certificateHash)
    }

    override fun hashCode(): Int {
        return certificateHash.contentHashCode()
    }

    fun getClearCertificate() = certificate.trimIndent()
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\\s".toRegex(), "")

}
