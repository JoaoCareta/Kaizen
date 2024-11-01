package com.joao.otavio.kaizen.data.remote

import com.joao.otavio.kaizen.domain.models.dto.KaizenApiDto
import retrofit2.http.GET

interface EventsApi {

    @GET("MockSports/sports.json")
    suspend fun getSportsEvents(): KaizenApiDto
}
