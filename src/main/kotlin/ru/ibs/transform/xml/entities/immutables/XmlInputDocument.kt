package ru.ibs.transform.xml.entities.immutables

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity
data class XmlInputDocument(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val xmlInputHash: ByteArray,

    @Column(columnDefinition = "TEXT")
    val xmlData: String,

    val createdAt: Instant = Instant.now(),

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XmlInputDocument

        return xmlInputHash.contentEquals(other.xmlInputHash)
    }

    override fun hashCode(): Int {
        return xmlInputHash.contentHashCode()
    }

    override fun toString(): String {
        return "XmlInputDocument(id=$id, xmlData='$xmlData')"
    }

}
