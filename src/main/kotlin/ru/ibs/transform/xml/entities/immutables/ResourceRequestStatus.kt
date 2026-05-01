package ru.ibs.transform.xml.entities.immutables

enum class ResourceRequestStatus {
    INITIAL,
    REJECTION_AFTER_FIRST,
    APPROVAL_AFTER_FIRST,
    REJECTION_AFTER_SECOND,
    APPROVAL_AFTER_SECOND,
    RESOURCES_NOT_FOUND,
    RESOURCES_FOUND,
    RESOURCES_READY_FOR_ISSUANCE,
    RESOURCES_ISSUED;
}
