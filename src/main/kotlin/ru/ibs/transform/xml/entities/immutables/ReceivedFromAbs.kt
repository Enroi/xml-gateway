package ru.ibs.transform.xml.entities.immutables

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import kotlin.math.min

@Entity
data class ReceivedFromAbs(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val jsonSentAbsHash: ByteArray,

    @Column(columnDefinition = "TEXT")
    val jsonSentAbsJson: String,

    @Column(columnDefinition = "TEXT")
    val xmlAbsAnswer: String,

    val xmlAbsAnswerHash: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReceivedFromAbs

        return xmlAbsAnswerHash.contentEquals(other.xmlAbsAnswerHash)
    }

    override fun hashCode(): Int = xmlAbsAnswerHash.contentHashCode()

    override fun toString(): String =
        "ReceivedFromAbs(id=$id, xmlAbsAnswer='${xmlAbsAnswer.substring(0, min(0, xmlAbsAnswer.length - 1))}')"

}
