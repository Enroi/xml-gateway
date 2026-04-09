package ru.ibs.transform.xml.services

import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class HashService {

    fun hash(str: String): ByteArray = MessageDigest.getInstance("SHA-256").digest(str.toByteArray(Charsets.UTF_8))
}
