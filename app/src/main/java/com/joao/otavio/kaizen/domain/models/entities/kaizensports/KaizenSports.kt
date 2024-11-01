package com.joao.otavio.kaizen.domain.models.entities.kaizensports

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

@Entity(
    tableName = KaizenSportsDataBaseConstants.activeSportsTableName,
    primaryKeys = [KaizenSportsDataBaseConstants.eventId]
)
data class KaizenSports(
    @ColumnInfo(name = KaizenSportsDataBaseConstants.eventId) val eventId: Int = Random.nextInt(),
    @ColumnInfo(name = KaizenSportsDataBaseConstants.sportId) val sportId: String,
    @ColumnInfo(name = KaizenSportsDataBaseConstants.sportName) val sportName: String,
    @TypeConverters(EventDetailConverter::class) @ColumnInfo(name = KaizenSportsDataBaseConstants.activeSportEvents) val activeSportEvents: List<EventDetail>
)

data class EventDetail(
    val eventId: String,
    val sportId: String,
    val eventName: String,
    val eventStartTime: Int,
    var isEventFavorite: Boolean = false
)

class EventDetailConverter {
    @TypeConverter
    fun fromEventDetailList(value: List<EventDetail>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toEventDetailList(value: String): List<EventDetail> {
        val listType = object : TypeToken<List<EventDetail>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
