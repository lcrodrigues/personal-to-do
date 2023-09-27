package com.example.personaltodo.featureevents.domain.usecase

import com.example.personaltodo.core.util.Resource
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class GetEvents(private val repository: EventRepository) {
    suspend operator fun invoke(): Flow<Resource<List<Event>>> {
        return repository.getEvents()
    }
}