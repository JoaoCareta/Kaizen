package com.joao.otavio.kaizen.di

import android.content.Context
import androidx.room.Room
import com.joao.otavio.kaizen.data.local.dao.FavoriteEventsDao
import com.joao.otavio.kaizen.data.local.dao.KaizenSportsDao
import com.joao.otavio.kaizen.data.local.database.KaizenSportsDataBase
import com.joao.otavio.kaizen.data.local.database.KaizenSportsDataBase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): KaizenSportsDataBase {
        return Room.databaseBuilder(
            context,
            KaizenSportsDataBase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideKaizenSportsDao(
        kaizenSportsDataBase: KaizenSportsDataBase
    ): KaizenSportsDao {
        return kaizenSportsDataBase.kaizenSportsDAO()
    }

    @Singleton
    @Provides
    fun provideKaizenFavoriteEventsDAO(
        kaizenSportsDataBase: KaizenSportsDataBase
    ): FavoriteEventsDao {
        return kaizenSportsDataBase.kaizenFavoriteEventsDAO()
    }

}
