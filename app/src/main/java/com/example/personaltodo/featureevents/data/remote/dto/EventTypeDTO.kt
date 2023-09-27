package com.example.personaltodo.featureevents.data.remote.dto

import com.example.personaltodo.featureevents.domain.model.EventType

data class EventTypeDTO(
    val id: Int,
    val name: String
)

fun EventTypeDTO.toEventType() = EventType(
    id,
    name
)