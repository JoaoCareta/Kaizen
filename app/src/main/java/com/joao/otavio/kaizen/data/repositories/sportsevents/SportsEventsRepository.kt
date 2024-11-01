package com.joao.otavio.kaizen.data.repositories.sportsevents

import com.joao.otavio.kaizen.domain.models.events.KaizenSports

interface SportsEventsRepository {
    suspend fun getAllSportsEvents(): KaizenSports
    suspend fun getAllSportsFromDataBase(): KaizenSports
}
