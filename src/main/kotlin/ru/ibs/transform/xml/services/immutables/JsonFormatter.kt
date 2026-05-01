package ru.ibs.transform.xml.services.immutables

import org.springframework.stereotype.Service
import tools.jackson.databind.SerializationFeature
import tools.jackson.databind.json.JsonMapper

@Service
class JsonFormatter() {

    fun format(obj: Any) = objectMapper.writeValueAsString(obj)
        ?: throw RuntimeException("Не удалось создать строку для входящего объекта: $obj")

    companion object {
        private val objectMapper = JsonMapper.builder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build()
    }

}
