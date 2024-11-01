package com.joao.otavio.kaizen.data.repositories.sportsevents

import com.joao.otavio.kaizen.data.datasources.local.sportsevents.SportsEventsLocalDataSource
import com.joao.otavio.kaizen.data.datasources.remote.SportsEventsRemoteDataSource
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.mappers.toEntity
import javax.inject.Inject

class SportsEventsRepositoryImpl @Inject constructor(
    private val sportsEventsRemoteDataSource: SportsEventsRemoteDataSource,
    private val sportsEventsLocalDataSource: SportsEventsLocalDataSource
): SportsEventsRepository {

    override suspend fun getAllSportsEvents(): KaizenSports {
        val allNewEvents = sportsEventsRemoteDataSource.getAllSportsEvents()
        val oldEvents = sportsEventsLocalDataSource.getAllSportsEvents()
        if (allNewEvents.activeSports.isEmpty()) return oldEvents
        return if (sportsEventsLocalDataSource.saveSportsIntoDatabase(allNewEvents.toEntity())) {
            allNewEvents
        } else {
            oldEvents
        }
    }

    override suspend fun getAllSportsFromDataBase(): KaizenSports {
        return sportsEventsLocalDataSource.getAllSportsEvents()
    }
}
