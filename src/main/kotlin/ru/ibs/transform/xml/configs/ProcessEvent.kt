package ru.ibs.transform.xml.configs

import org.springframework.context.ApplicationEvent
import ru.ibs.transform.xml.controllers.ResourceRequestDTO

class ProcessEvent(
    source: ResourceRequestDTO,
) : ApplicationEvent(source)
