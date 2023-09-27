package com.example.personaltodo.featureevents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.personaltodo.featureevents.data.local.entity.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class EventDatabase: RoomDatabase() {

    abstract val dao: EventDao
}