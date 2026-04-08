package ru.ibs.transform.xml

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withAllAnnotationsOf
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

class ArchTests {

    @Test
    fun `Entry point ends with Application`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAllAnnotationsOf(SpringBootApplication::class)
            .assertTrue(additionalMessage = "Наименование входного класса должно заканчиваться на Application") { it.name.endsWith("Application") }
    }

    @Test
    fun `Conffigurations in config package`() =
        Konsist
            .scopeFromProject()
            .classes()
            .withAllAnnotationsOf(Configuration::class)
            .assertTrue(additionalMessage = "Классы конфигурации должны быть в пакете конфигураций") { it.packagee?.name == "ru.ibs.transform.xml.configs" }
}
