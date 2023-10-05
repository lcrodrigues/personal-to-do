package com.example.personaltodo.featureevents.data.repository

import com.example.personaltodo.core.util.Resource
import com.example.personaltodo.featureevents.data.local.EventDao
import com.example.personaltodo.featureevents.data.local.entity.toEvent
import com.example.personaltodo.featureevents.data.remote.EventApi
import com.example.personaltodo.featureevents.data.remote.dto.toEventEntity
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.domain.repository.EventRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class EventRepositoryImpl(
    private val api: EventApi,
    private val dao: EventDao
) : EventRepository {

    override fun getEvents() = flow<Resource<List<Event>>> {
        val localEvents = dao.getEvents().map { it.toEvent() }
        emit(Resource.Success(data = localEvents))
        
        try {
            val remoteEvents = api.getEvents()
            dao.apply {
                deleteEvents(eventsName = remoteEvents.map { it.name })
                insertEvents(remoteEvents.map { it.toEventEntity() })
            }

            emit(Resource.Success(data = dao.getEvents().map { it.toEvent() }))
        } catch (e: HttpException) {
            Resource.Error(error = e, data = localEvents)
        } catch (e: IOException) {
            Resource.Error(error = e, data = localEvents)
        }
    }
}