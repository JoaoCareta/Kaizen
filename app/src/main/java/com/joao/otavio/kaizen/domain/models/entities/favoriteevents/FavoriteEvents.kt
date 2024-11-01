package com.joao.otavio.kaizen.domain.models.entities.favoriteevents

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = FavoriteEventsDataBaseConstants.favoriteEventsTableName,
    primaryKeys = [FavoriteEventsDataBaseConstants.eventId]
)
data class FavoriteEvents(
    @ColumnInfo(name = FavoriteEventsDataBaseConstants.eventId) val eventId: String,
    @ColumnInfo(name = FavoriteEventsDataBaseConstants.sportId) val sportId: String,
    @ColumnInfo(name = FavoriteEventsDataBaseConstants.eventName) val eventName: String,
    @ColumnInfo(name = FavoriteEventsDataBaseConstants.eventStartTime) val eventStartTime: Int,
)
