package com.joao.otavio.kaizen.data.datasources.local.sportsevents

import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.KaizenSports as KaizenSportsEntity

interface SportsEventsLocalDataSource {
    suspend fun saveSportsIntoDatabase(sportsEvents: List<KaizenSportsEntity>): Boolean
    suspend fun getAllSportsEvents(): KaizenSports
}
