package com.example.personaltodo.featureevents.domain.model

data class EventType(
    val id: Int,
    val name: String
)

fun EventType.isValid(): Boolean {
    return id != -1 && name.isNotEmpty()
}