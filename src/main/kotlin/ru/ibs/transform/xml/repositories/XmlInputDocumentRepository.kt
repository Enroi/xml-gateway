package ru.ibs.transform.xml.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.ibs.transform.xml.entities.immutables.XmlInputDocument

@Repository
interface XmlInputDocumentRepository: JpaRepository<XmlInputDocument, Long>
