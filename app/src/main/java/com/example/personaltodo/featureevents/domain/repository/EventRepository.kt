package com.example.personaltodo.featureevents.domain.repository

import com.example.personaltodo.core.util.Resource
import com.example.personaltodo.featureevents.domain.model.Event

interface EventRepository {
    suspend fun getEvents(): Resource<List<Event>>
}