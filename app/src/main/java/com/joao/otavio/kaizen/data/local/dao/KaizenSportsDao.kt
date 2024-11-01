package com.joao.otavio.kaizen.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.KaizenSports
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.KaizenSportsDataBaseConstants

@Dao
abstract class KaizenSportsDao: BaseUpsertDao<KaizenSports>() {
    @Query("SELECT * FROM ${KaizenSportsDataBaseConstants.activeSportsTableName}")
    abstract suspend fun getAllSports(): List<KaizenSports>

    @Query("DELETE FROM ${KaizenSportsDataBaseConstants.activeSportsTableName}")
    abstract suspend fun deleteAllSports(): Int
}