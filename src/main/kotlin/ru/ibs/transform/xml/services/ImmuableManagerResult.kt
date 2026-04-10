package ru.ibs.transform.xml.services

data class ImmuableManagerResult(
    val status: ImmutableProcessStatuses,
    val xmlResult: String?,
)
