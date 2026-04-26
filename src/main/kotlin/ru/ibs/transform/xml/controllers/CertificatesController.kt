package ru.ibs.transform.xml.controllers

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ibs.transform.xml.services.immutables.CertificatesService

@RestController
@RequestMapping("/api/certificates")
class CertificatesController(
    private val certificatesService: CertificatesService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping(value = ["enable"])
    fun enableCertificate(@RequestBody certificate: String) {
        log.info("Enable certificate $certificate")
        certificatesService.enableCertificate(certificate)
    }

    @PostMapping(value= ["/disable"])
    fun disableCertificate(@RequestBody certificate: String) {
        log.info("Disable certificate $certificate")
        certificatesService.disableCertificate(certificate)
    }
}
