package com.joao.otavio.kaizen.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.joao.otavio.kaizen.data.local.dao.FavoriteEventsDao
import com.joao.otavio.kaizen.data.local.dao.KaizenSportsDao
import com.joao.otavio.kaizen.data.local.database.KaizenSportsDataBase.Companion.DATABASE_VERSION_1
import com.joao.otavio.kaizen.data.local.database.KaizenSportsDataBase.Companion.DATABASE_VERSION_2
import com.joao.otavio.kaizen.data.local.database.KaizenSportsDataBase.Companion.KAIZEN_DATABASE_CURRENT_VERSION
import com.joao.otavio.kaizen.domain.models.entities.favoriteevents.FavoriteEvents
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.EventDetailConverter
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.KaizenSports

@Database(
    version = KAIZEN_DATABASE_CURRENT_VERSION,
    entities = [
        KaizenSports::class,
        FavoriteEvents::class
    ],
    autoMigrations = [
        AutoMigration(from = DATABASE_VERSION_1, to = DATABASE_VERSION_2)
    ],
    exportSchema = true
)
@TypeConverters(EventDetailConverter::class)
abstract class KaizenSportsDataBase : RoomDatabase() {

    abstract fun kaizenSportsDAO(): KaizenSportsDao

    abstract fun kaizenFavoriteEventsDAO(): FavoriteEventsDao

    companion object {
        const val DATABASE_VERSION_1 = 1
        const val DATABASE_VERSION_2 = 2
        const val KAIZEN_DATABASE_CURRENT_VERSION = DATABASE_VERSION_2
        const val DATABASE_NAME = "kaizen_sports_database"
    }
}
