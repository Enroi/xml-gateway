package ru.ibs.transform.xml.services.immutables

data class ImmuableManagerResult(
    val status: ImmutableProcessStatuses,
    val xmlResult: String?,
)
