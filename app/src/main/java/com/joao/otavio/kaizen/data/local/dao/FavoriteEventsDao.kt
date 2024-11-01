package com.joao.otavio.kaizen.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.joao.otavio.kaizen.domain.models.entities.favoriteevents.FavoriteEventsDataBaseConstants
import com.joao.otavio.kaizen.domain.models.entities.favoriteevents.FavoriteEvents

@Dao
abstract class FavoriteEventsDao : BaseUpsertDao<FavoriteEvents>() {

    @Query("SELECT * FROM ${FavoriteEventsDataBaseConstants.favoriteEventsTableName} WHERE ${FavoriteEventsDataBaseConstants.sportId} = :sportId")
    abstract suspend fun getFavoriteEventsBySportId(sportId: String): List<FavoriteEvents>
}
