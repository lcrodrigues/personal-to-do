package com.example.personaltodo.featureevents.domain.repository

import com.example.personaltodo.core.util.Resource
import com.example.personaltodo.featureevents.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<Resource<List<Event>>>
}