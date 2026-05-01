package ru.ibs.transform.xml.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import ru.ibs.transform.xml.entities.immutables.ResourceRequestStatus
import java.time.Instant


data class ResourceRequestDTO(

    @JsonProperty("request-id")
    val requestId: String,

    val status: ResourceRequestStatus,

    @JsonProperty("resource")
    val resourceName: String
)
