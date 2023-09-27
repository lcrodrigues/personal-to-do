package com.example.personaltodo.featureevents.di

import android.app.Application
import androidx.room.Room
import com.example.personaltodo.featureevents.data.local.Converters
import com.example.personaltodo.featureevents.data.local.EventDatabase
import com.example.personaltodo.featureevents.data.remote.EventApi
import com.example.personaltodo.featureevents.data.repository.EventRepositoryImpl
import com.example.personaltodo.featureevents.data.util.GsonParser
import com.example.personaltodo.featureevents.domain.repository.EventRepository
import com.example.personaltodo.featureevents.domain.usecase.GetEvents
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {

    @Provides
    @Singleton
    fun provideGetEventsUseCase(repository: EventRepository): GetEvents {
        return GetEvents(repository)
    }

    @Provides
    @Singleton
    fun provideEventRepository(api: EventApi, db: EventDatabase): EventRepository {
        return EventRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideEventDatabase(app: Application): EventDatabase {
        return Room
            .databaseBuilder(app, EventDatabase::class.java, "event_db")
            .addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideEventsApi(): EventApi {
        return Retrofit
            .Builder()
            .baseUrl(EventApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApi::class.java)
    }
}