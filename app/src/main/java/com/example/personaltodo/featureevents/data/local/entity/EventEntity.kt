package com.example.personaltodo.featureevents.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.domain.model.EventType
import com.example.personaltodo.featureevents.domain.model.SuggestedBy

@Entity
data class EventEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isDone: String,
    val eventType: EventType,
    val suggestedBy: SuggestedBy,
    val description: String? = null,
    val date: String? = null,
    val address: String? = null,
    val rating: Int? = null
)

fun EventEntity.toEvent() = Event(
    id, name, isDone, eventType, suggestedBy, description, date, address, rating
)