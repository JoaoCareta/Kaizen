package com.joao.otavio.kaizen.data.datasources.local.sportsevents

import android.util.Log
import com.joao.otavio.kaizen.data.local.dao.KaizenSportsDao
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.mappers.toDomain
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.KaizenSports as KaizenSportsEntity
import com.joao.otavio.kaizen.utils.Constants.KAIZEN_LOGS_TAG
import java.lang.Exception
import javax.inject.Inject

class SportsEventsLocalDataSourceImpl @Inject constructor(
    private val kaizenSportsDao: KaizenSportsDao
): SportsEventsLocalDataSource {

    override suspend fun saveSportsIntoDatabase(sportsEvents: List<KaizenSportsEntity>): Boolean {
        return try {
            if (kaizenSportsDao.deleteAllSports() >= 0) {
                kaizenSportsDao.upsertAll(sportsEvents)
            } else {
                Log.e(KAIZEN_LOGS_TAG, "unable to save into database")
                false
            }
        } catch (e: Exception) {
            Log.e(KAIZEN_LOGS_TAG, e.message.toString())
            false
        }
    }

    override suspend fun getAllSportsEvents(): KaizenSports {
        return try {
            val allSportsEvents = kaizenSportsDao.getAllSports().toDomain()
            allSportsEvents
        } catch (e: Exception) {
            Log.e(KAIZEN_LOGS_TAG, e.message.toString())
            KaizenSports(emptyList())
        }
    }
}
