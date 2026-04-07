package ru.ibs.transform.xml.controllers

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result")
class ResultController {

    data class SingleResult (
        val id: Long,
        val dateInput: LocalDateTime,
        val dateOutput: LocalDateTime,
    )

    data class BatchResults (
        val data: List<SingleResult>,
        val page: Int,
        val pages: Int,
        val rows: Int,
    )

    @GetMapping("all")
    fun results(): BatchResults {
        return BatchResults(
            listOf(
                SingleResult(1, LocalDateTime.now(), LocalDateTime.now()),
                SingleResult(2, LocalDateTime.now(), LocalDateTime.now()),
            ), 0, 1, 10)
    }

    @PostMapping("all2")
    fun results2(): BatchResults {
        return BatchResults(
            listOf(
                SingleResult(1, LocalDateTime.now(), LocalDateTime.now()),
                SingleResult(2, LocalDateTime.now(), LocalDateTime.now()),
            ), 0, 1, 10)
    }
}
