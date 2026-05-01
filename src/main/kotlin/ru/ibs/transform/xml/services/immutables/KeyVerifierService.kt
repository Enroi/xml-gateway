package ru.ibs.transform.xml.services.immutables

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.ibs.transform.xml.repositories.immutables.CertficateDisabledRepository
import ru.ibs.transform.xml.repositories.immutables.CertificateEnabledRepository
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import kotlin.io.encoding.Base64

@Service
class KeyVerifierService(
    private val certificateEnabledRepository: CertificateEnabledRepository,
    private val certificateDisabledRepository: CertficateDisabledRepository,
    private val hashService: HashService,
    ) {

    fun verifyText(text: String, signatureBytes: ByteArray, certificateParam: String): Boolean {

        val certificateHash = hashService.hash(certificateParam)
        val certificate = certificateEnabledRepository.findByCertificateHash(certificateHash) ?: run { throw SecurityException("Unknown Certificate: $certificateParam") }
        val certificateDisabled = certificateDisabledRepository.existsByCertificateHash(certificateHash)
        if (certificateDisabled) {
            throw SecurityException("Certificate disabled: $certificateParam")
        }

        val keyBytes = Base64.decode(certificate.getClearCertificate())
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(keySpec)
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(text.toByteArray(Charsets.UTF_8))
        return signature.verify(signatureBytes)
    }

}
