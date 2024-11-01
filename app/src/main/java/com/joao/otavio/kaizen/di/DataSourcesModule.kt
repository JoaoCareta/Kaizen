package com.joao.otavio.kaizen.di

import com.joao.otavio.kaizen.data.datasources.local.favoriteevents.FavoriteEventsLocalDataSource
import com.joao.otavio.kaizen.data.datasources.local.favoriteevents.FavoriteEventsLocalDataSourceImpl
import com.joao.otavio.kaizen.data.datasources.local.sportsevents.SportsEventsLocalDataSource
import com.joao.otavio.kaizen.data.datasources.local.sportsevents.SportsEventsLocalDataSourceImpl
import com.joao.otavio.kaizen.data.datasources.remote.SportsEventsRemoteDataSource
import com.joao.otavio.kaizen.data.datasources.remote.SportsEventsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourcesModule {

    @Binds
    abstract fun bindSportsEventsRemoteDataSource(
        sportsEventsRemoteDataSourceImpl: SportsEventsRemoteDataSourceImpl
    ): SportsEventsRemoteDataSource

    @Binds
    abstract fun bindSportsEventsLocalDataSource(
        sportsEventsLocalDataSourceImpl: SportsEventsLocalDataSourceImpl
    ): SportsEventsLocalDataSource

    @Binds
    abstract fun bindFavoriteEventLocalDataSource(
        favoriteEventsLocalDataSourceImpl: FavoriteEventsLocalDataSourceImpl
    ): FavoriteEventsLocalDataSource
}
