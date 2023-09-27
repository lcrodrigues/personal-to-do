package com.example.personaltodo.featureevents.domain.usecase

import com.example.personaltodo.core.util.Resource
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.domain.repository.EventRepository

class GetEvents(private val repository: EventRepository) {
    suspend operator fun invoke(): Resource<List<Event>> {
        return repository.getEvents()
    }
}