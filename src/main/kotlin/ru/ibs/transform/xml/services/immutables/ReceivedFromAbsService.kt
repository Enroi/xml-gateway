package ru.ibs.transform.xml.services.immutables

import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.ReceivedFromAbs
import ru.ibs.transform.xml.entities.immutables.SentToAbs
import ru.ibs.transform.xml.repositories.ReceivedFromAbsRepository
import java.time.Instant

@Service
class ReceivedFromAbsService(
    private val receivedMessageRepository: ReceivedFromAbsRepository,
    private val canonicalService: CanonicalService,
) {

    fun merge(
        jsonSentAbsHash: ByteArray,
        jsonSentAbsJson: String,
        xmlAbsAnswer: String,
        xmlAbsAnswerHash: ByteArray,
        createdAt: Instant
    ) = receivedMessageRepository.merge(jsonSentAbsHash, jsonSentAbsJson, xmlAbsAnswer, xmlAbsAnswerHash, createdAt)

    fun findBySentToAbs(sentToAbs: SentToAbs): ReceivedFromAbs? {
        return receivedMessageRepository.findByJsonSentAbsHash(sentToAbs.jsonSentAbsHash)
    }
}
