package ru.ibs.transform.xml.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.ibs.transform.xml.entities.immutables.SentToAbs

interface SentToAbsRepository: JpaRepository<SentToAbs, Long>
