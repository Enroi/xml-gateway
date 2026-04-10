package ru.ibs.transform.xml.services

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.entities.immutables.SentToAbs

@Service
class AbsEmulatorService {

    @Async
    fun emulateForSentToAbs(sentToAbs: SentToAbs) {

    }
}
