package ru.ibs.transform.xml.entities.immutables

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.time.Instant

@Entity
data class SentToAbs(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(unique = true, nullable = false)
    val jsonSentAbsHash: ByteArray,

    @Column(columnDefinition = "TEXT")
    val jsonSentAbsJson: String,

    val xmlInputDocumentHash: ByteArray,

    val createdAt: Instant = Instant.now(),

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SentToAbs

        return jsonSentAbsHash.contentEquals(other.jsonSentAbsHash)
    }

    override fun hashCode(): Int {
        return jsonSentAbsHash.contentHashCode()
    }

    override fun toString(): String {
        return "SentToAbs(id=$id, jsonSentAbsJson='$jsonSentAbsJson')"
    }
}
