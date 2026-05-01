package ru.ibs.transform.xml.configs

import org.springframework.context.ApplicationEvent

class ProcessEvent(
    source: Any,
) : ApplicationEvent(source)
