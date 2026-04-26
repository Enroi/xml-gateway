package ru.ibs.transform.xml.services.immutables

import org.springframework.stereotype.Service
import ru.ibs.transform.xml.repositories.immutables.CertficateDisabledRepository
import ru.ibs.transform.xml.repositories.immutables.CertificateEnabledRepository

@Service
class CertificatesService(
    private val certificateEnabledRepository: CertificateEnabledRepository,
    private val ceritificateDisabledRepository: CertficateDisabledRepository,
    private val hashService: HashService,
) {

    fun enableCertificate(certificate: String) {

        certificateEnabledRepository.merge(
            certificate = certificate,
            certificateHash = hashService.hash(certificate),
        )

    }

    fun disableCertificate(certificate: String) {
        ceritificateDisabledRepository.merge(
            certificate = certificate,
            certificateHash = hashService.hash(certificate),
        )
    }
}
