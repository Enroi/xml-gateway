package ru.ibs.transform.xml

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withModifier
import com.lemonappdev.konsist.api.ext.list.objects
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withAllAnnotationsOf
import com.lemonappdev.konsist.api.ext.list.withAnnotationOf
import com.lemonappdev.konsist.api.ext.list.withName
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

class GeneralArchTests {

    @Test
    fun `Entry point ends with Application`() =
        Konsist
            .scopeFromProject()
            .classes()
            .withAllAnnotationsOf(SpringBootApplication::class)
            .assertTrue(additionalMessage = "Наименование входного класса должно заканчиваться на Application") { it.name.endsWith("Application") }

    @Test
    fun `Configurations in config package`() =
        Konsist
            .scopeFromProject()
            .classes()
            .withAllAnnotationsOf(Configuration::class)
            .assertTrue(additionalMessage = "Классы конфигурации должны быть в пакете конфигураций") { it.packagee?.name == "ru.ibs.transform.xml.configs" }

    @Test
    fun `classes with 'RestController' annotation should reside in 'controller' package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAnnotationOf(RestController::class)
            .assertTrue(additionalMessage = "Классы контроллеров должны находится в пакете controllers") { it.resideInPackage("..controllers..") }
    }

    @Test
    fun `classes with 'RestController' annotation should have 'Controller' suffix`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAnnotationOf(RestController::class)
            .assertTrue { it.hasNameEndingWith("Controller") }
    }

    @Test
    fun `Service classes should be annotated with Service annotation`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Service")
            .assertTrue { it.hasAnnotationOf(Service::class) }
    }

    @Test
    fun `RestControllers should not have state fields`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAnnotationOf(RestController::class)
            .objects()
            .withModifier(KoModifier.COMPANION)
            .assertTrue {
                it.properties().isEmpty()
            }
    }

    @Test
    fun `Controller should not have repository fields`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withName { it == "MessageController" }
            .withAnnotationOf(RestController::class)
            .properties()
            .assertTrue(additionalMessage = "Контроллер не должен работать с репозиториями") {
                (it.containingDeclaration as KoClassDeclaration).annotations.none { a -> a.equals(Repository::class) }
            }
    }
}
