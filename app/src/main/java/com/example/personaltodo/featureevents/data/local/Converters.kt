package com.example.personaltodo.featureevents.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.personaltodo.featureevents.data.util.JsonParser
import com.example.personaltodo.featureevents.domain.model.EventType
import com.example.personaltodo.featureevents.domain.model.SuggestedBy

@ProvidedTypeConverter
class Converters(private val jsonParser: JsonParser) {

    @TypeConverter
    fun fromEventTypeJson(json: String): EventType {
        return jsonParser.fromJson<EventType>(json, EventType::class.java)
            ?: EventType(
                id = -1,
                name = ""
            )
    }

    @TypeConverter
    fun toEventTypeJson(eventType: EventType): String {
        return jsonParser.toJson(eventType, EventType::class.java) ?: ""
    }

    @TypeConverter
    fun fromSuggestedByJson(json: String): SuggestedBy {
        return jsonParser.fromJson<SuggestedBy>(json, SuggestedBy::class.java)
            ?: SuggestedBy(
                id = -1,
                name = ""
            )
    }

    @TypeConverter
    fun toSuggestedByJson(suggestedBy: SuggestedBy): String {
        return jsonParser.toJson(suggestedBy, SuggestedBy::class.java) ?: ""
    }
}