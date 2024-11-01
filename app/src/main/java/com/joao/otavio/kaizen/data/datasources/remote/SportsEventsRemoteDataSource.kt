package com.joao.otavio.kaizen.data.datasources.remote

import com.joao.otavio.kaizen.domain.models.events.KaizenSports

interface SportsEventsRemoteDataSource {
    suspend fun getAllSportsEvents(): KaizenSports
}
