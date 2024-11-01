package com.joao.otavio.kaizen.data.repositories.favoriteevents

import com.joao.otavio.kaizen.domain.models.events.EventDetail

interface FavoriteEventsRepository {
    suspend fun getFavoriteEventsBySportId(sportId: String): List<EventDetail>
    suspend fun saveFavoriteEvent(favoriteEvents: EventDetail): Boolean
    suspend fun deleteFavoriteEvent(favoriteEvents: EventDetail): Boolean
}
