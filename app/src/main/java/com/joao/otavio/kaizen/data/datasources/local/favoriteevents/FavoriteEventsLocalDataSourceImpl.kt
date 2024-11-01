package com.joao.otavio.kaizen.data.datasources.local.favoriteevents

import android.util.Log
import com.joao.otavio.kaizen.data.local.dao.FavoriteEventsDao
import com.joao.otavio.kaizen.domain.models.entities.favoriteevents.FavoriteEvents
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.mappers.toEventDetail
import com.joao.otavio.kaizen.utils.Constants.KAIZEN_LOGS_TAG
import javax.inject.Inject

class FavoriteEventsLocalDataSourceImpl @Inject constructor(
    private val favoriteEventsDao: FavoriteEventsDao
): FavoriteEventsLocalDataSource {
    override suspend fun getFavoriteEventsBySportId(sportId: String): List<EventDetail> {
        return try {
            favoriteEventsDao.getFavoriteEventsBySportId(sportId).map { it.toEventDetail() }
        } catch (e: Exception) {
            Log.e(KAIZEN_LOGS_TAG, "fail to get favorite events into database")
            emptyList()
        }
    }

    override suspend fun saveFavoriteEvent(favoriteEvents: FavoriteEvents): Boolean {
        return try {
            favoriteEventsDao.upsert(favoriteEvents)
        } catch (e: Exception) {
            Log.e(KAIZEN_LOGS_TAG, "fail to save favorite event into database")
            false
        }
    }

    override suspend fun deleteFavoriteEvent(favoriteEvents: FavoriteEvents): Boolean {
        return try {
            favoriteEventsDao.delete(favoriteEvents)
            true
        } catch (e: Exception) {
            Log.e(KAIZEN_LOGS_TAG, "fail to delete favorite event into database")
            false
        }
    }
}
