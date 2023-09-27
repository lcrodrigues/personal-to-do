package com.example.personaltodo.featureevents.domain.model

data class Event(
    val id: Int,
    val name: String,
    val isDone: String,
    val eventType: EventType,
    val suggestedBy: SuggestedBy,
    val description: String? = null,
    val date: String? = null,
    val address: String? = null,
    val rating: Int? = null
)
