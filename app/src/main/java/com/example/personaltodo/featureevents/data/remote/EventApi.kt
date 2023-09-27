package com.example.personaltodo.featureevents.data.remote

import com.example.personaltodo.featureevents.data.remote.dto.EventDTO
import retrofit2.http.GET

interface EventApi {

    @GET("event")
    suspend fun getEvents(): List<EventDTO>

    companion object {
        const val BASE_URL = "https://futebolnatv.proxy.beeceptor.com/"
    }
}