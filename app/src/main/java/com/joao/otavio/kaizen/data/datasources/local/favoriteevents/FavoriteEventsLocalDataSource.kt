package com.joao.otavio.kaizen.data.datasources.local.favoriteevents

import com.joao.otavio.kaizen.domain.models.entities.favoriteevents.FavoriteEvents
import com.joao.otavio.kaizen.domain.models.events.EventDetail

interface FavoriteEventsLocalDataSource {
    suspend fun getFavoriteEventsBySportId(sportId: String): List<EventDetail>
    suspend fun saveFavoriteEvent(favoriteEvents: FavoriteEvents): Boolean
    suspend fun deleteFavoriteEvent(favoriteEvents: FavoriteEvents): Boolean
}
