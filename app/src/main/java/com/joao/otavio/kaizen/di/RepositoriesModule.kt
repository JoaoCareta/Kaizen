package com.joao.otavio.kaizen.di

import com.joao.otavio.kaizen.data.repositories.favoriteevents.FavoriteEventsRepository
import com.joao.otavio.kaizen.data.repositories.favoriteevents.FavoriteEventsRepositoryImpl
import com.joao.otavio.kaizen.data.repositories.sportsevents.SportsEventsRepository
import com.joao.otavio.kaizen.data.repositories.sportsevents.SportsEventsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindSportsEventsRepository(
        sportsEventsRepositoryImpl: SportsEventsRepositoryImpl
    ): SportsEventsRepository

    @Binds
    abstract fun bindFavoriteEventsRepository(
        favoriteEventsRepositoryImpl: FavoriteEventsRepositoryImpl
    ): FavoriteEventsRepository
}
