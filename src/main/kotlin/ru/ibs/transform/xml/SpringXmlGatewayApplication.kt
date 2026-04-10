package ru.ibs.transform.xml

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class SpringXmlGatewayApplication

fun main(args: Array<String>) {
    runApplication<SpringXmlGatewayApplication>(*args)
}
