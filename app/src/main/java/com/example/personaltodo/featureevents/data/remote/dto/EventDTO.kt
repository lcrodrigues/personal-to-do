package com.example.personaltodo.featureevents.data.remote.dto

import com.example.personaltodo.featureevents.data.local.entity.EventEntity
import com.example.personaltodo.featureevents.domain.model.Event

data class EventDTO(
    val id: Int,
    val name: String,
    val isDone: String,
    val eventType: EventTypeDTO,
    val suggestedBy: SuggestedByDTO,
    val description: String? = null,
    val date: String? = null,
    val address: String? = null,
    val rating: Int? = null
)

fun EventDTO.toEvent() = Event(
    id,
    name,
    isDone,
    eventType.toEventType(),
    suggestedBy.toSuggestedBy(),
    description,
    date,
    address,
    rating
)

fun EventDTO.toEventEntity() = EventEntity(
    id,
    name,
    isDone,
    eventType.toEventType(),
    suggestedBy.toSuggestedBy(),
    description,
    date,
    address,
    rating
)