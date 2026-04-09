package ru.ibs.transform.xml.services

import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import kotlin.io.encoding.Base64

@Service
class KeyVerifierService {

    fun verifyText(text: String, signatureBytes: ByteArray, certificateName: String): Boolean {
        val certificate: String = publicKeys[certificateName] ?: run { throw SecurityException("Unknown Certificate: $certificateName") }

        val keyBytes = Base64.decode(certificate)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(keySpec)
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(text.toByteArray(Charsets.UTF_8))
        return signature.verify(signatureBytes)
    }

    companion object {
        val certificate1 = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm7+e9QLoR6TLcqHva1xD
            9TwpSqWdJ2ff0VSZc3xL2UJ2gbIW4bG8DPD5N7ylQKdsDRxuGZtv01OZpevWt7u9
            alqTtPpYCbp7u9Ukklwma1/SDtI9t2FHVFSCYoRw7pPe170nVaKv9UWAblbnnhcT
            bNGLsELXchw37bz6M5zt9OAznN+BPnJvYX3/smdofhJoDybwYXkgicBazZHhHiwJ
            TQGQxxQMUexGNXui8TQ5MU0dZYBUYinv12Beb7Atim3Jg5Rn9WfvKPSLqWnrVUxl
            8bh1Y9/8CGA5GXzF1UXjHH7jxBSfk3ZEV/3lObAcG9KSNWq3tSmywI1ppb+YCOt1
            HwIDAQAB
            -----END PUBLIC KEY-----
        """.trimIndent()
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        val publicKeys = mapOf("certificate 1" to certificate1)
    }
}
