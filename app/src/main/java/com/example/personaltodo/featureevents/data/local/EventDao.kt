package com.example.personaltodo.featureevents.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.personaltodo.featureevents.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM evententity")
    suspend fun getEvents(): List<EventEntity>

    @Query("SELECT * FROM evententity WHERE name LIKE '%' || :eventName || '%'")
    suspend fun getEventsByName(eventName: String): List<EventEntity>

    @Query("DELETE FROM evententity WHERE name IN(:eventsName)")
    suspend fun deleteEvents(eventsName: List<String>)
}