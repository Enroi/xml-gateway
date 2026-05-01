package ru.ibs.transform.xml.entities.immutables

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import ru.ibs.transform.xml.controllers.ResourceRequestDTO
import java.time.Instant
import kotlin.math.min

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["jsonHash", "document_type"])]
)
data class ResourceRequestDocument(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false)
    val jsonHash: ByteArray,

    @Column(columnDefinition = "TEXT")
    val jsonData: String,

    val createdAt: Instant = Instant.now(),

    @Enumerated(EnumType.STRING)
    val documentType: ResourceRequestStatus,

    val parentDocumentHash: ByteArray?,

    @Column(columnDefinition = "TEXT")
    val parentDocumentJson: String?,

    val resourceName: String,

    val requestTimeGeneration: Instant,

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResourceRequestDocument

        return jsonHash.contentEquals(other.jsonHash)
    }

    override fun hashCode(): Int {
        return jsonHash.contentHashCode()
    }

    override fun toString(): String {
        return "ResourceRequestDocument(id=$id, xmlData='${jsonData.substring(0, min(50, jsonData.length))}...')"
    }

    fun toDto() = ResourceRequestDTO(
        timeGeneration = requestTimeGeneration,
        status = documentType,
        resourceName = resourceName,
    )
}
