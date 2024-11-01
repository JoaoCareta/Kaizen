package com.joao.otavio.kaizen.data.datasources.remote

import android.util.Log
import com.joao.otavio.kaizen.data.remote.EventsApi
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.mappers.toSportDetail
import com.joao.otavio.kaizen.utils.Constants.KAIZEN_LOGS_TAG
import javax.inject.Inject

class SportsEventsRemoteDataSourceImpl @Inject constructor(
    private val eventsApi: EventsApi
): SportsEventsRemoteDataSource {
    override suspend fun getAllSportsEvents(): KaizenSports {
        return try {
            val response = eventsApi.getSportsEvents()
            KaizenSports(
                activeSports = response.map { it.toSportDetail() }
            )
        } catch (e: Throwable) {
            Log.e(KAIZEN_LOGS_TAG, e.message.toString())
            KaizenSports(emptyList())
        }
    }
}
