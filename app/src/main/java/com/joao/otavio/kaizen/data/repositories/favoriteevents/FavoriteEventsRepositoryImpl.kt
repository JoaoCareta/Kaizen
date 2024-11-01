package com.joao.otavio.kaizen.data.repositories.favoriteevents

import com.joao.otavio.kaizen.data.datasources.local.favoriteevents.FavoriteEventsLocalDataSource
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.mappers.toFavoriteEvents
import javax.inject.Inject

class FavoriteEventsRepositoryImpl @Inject constructor(
    private val favoriteEventsLocalDataSource: FavoriteEventsLocalDataSource
) : FavoriteEventsRepository {
    override suspend fun getFavoriteEventsBySportId(sportId: String): List<EventDetail> {
        return favoriteEventsLocalDataSource.getFavoriteEventsBySportId(sportId)
    }

    override suspend fun saveFavoriteEvent(favoriteEvents: EventDetail): Boolean {
        return favoriteEventsLocalDataSource.saveFavoriteEvent(favoriteEvents.toFavoriteEvents())
    }

    override suspend fun deleteFavoriteEvent(favoriteEvents: EventDetail): Boolean {
        return favoriteEventsLocalDataSource.deleteFavoriteEvent(favoriteEvents.toFavoriteEvents())
    }
}
