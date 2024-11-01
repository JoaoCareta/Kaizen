package com.joao.otavio.kaizen.data.repositories.sportsevents

import com.joao.otavio.kaizen.data.datasources.local.sportsevents.SportsEventsLocalDataSource
import com.joao.otavio.kaizen.data.datasources.remote.SportsEventsRemoteDataSource
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.events.SportDetail
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SportsEventsRepositoryImplTest {

    private val sportsEventsRemoteDataSource: SportsEventsRemoteDataSource = mockk()
    private val sportsEventsLocalDataSource: SportsEventsLocalDataSource = mockk()
    private lateinit var sportsEventsRepositoryImpl: SportsEventsRepositoryImpl

    @Before
    fun setUp() {
        sportsEventsRepositoryImpl = SportsEventsRepositoryImpl(
            sportsEventsRemoteDataSource,
            sportsEventsLocalDataSource
        )
    }

    @After
    fun tearDown() {
        confirmVerified(sportsEventsRemoteDataSource, sportsEventsLocalDataSource)
    }

    @Test
    fun `getAllSportsEvents should return remote data when local save is successful`() = runBlocking {
        // Mockk
        val remoteKaizenSports = createMockKaizenSports()
        val localKaizenSports = createMockKaizenSports()
        coEvery { sportsEventsRemoteDataSource.getAllSportsEvents() } returns remoteKaizenSports
        coEvery { sportsEventsLocalDataSource.getAllSportsEvents() } returns localKaizenSports
        coEvery { sportsEventsLocalDataSource.saveSportsIntoDatabase(any()) } returns true

        // Run test
        val result = sportsEventsRepositoryImpl.getAllSportsEvents()

        // Assert
        assertEquals(remoteKaizenSports, result)
        coVerify {
            sportsEventsRemoteDataSource.getAllSportsEvents()
            sportsEventsLocalDataSource.saveSportsIntoDatabase(any())
            sportsEventsLocalDataSource.getAllSportsEvents()
        }
    }

    @Test
    fun `getAllSportsEvents should return local data when remote data is an emptyListOf`() = runBlocking {
        // Mockk
        val remoteKaizenSports = KaizenSports(emptyList())
        val localKaizenSports = createMockKaizenSports()
        coEvery { sportsEventsRemoteDataSource.getAllSportsEvents() } returns remoteKaizenSports
        coEvery { sportsEventsLocalDataSource.getAllSportsEvents() } returns localKaizenSports
        coEvery { sportsEventsLocalDataSource.saveSportsIntoDatabase(any()) } returns true

        // Run test
        val result = sportsEventsRepositoryImpl.getAllSportsEvents()

        // Assert
        assertEquals(localKaizenSports, result)
        coVerify {
            sportsEventsRemoteDataSource.getAllSportsEvents()
            sportsEventsLocalDataSource.getAllSportsEvents()
        }
    }

    @Test
    fun `getAllSportsFromDataBase should return all KaizenSports from database`() = runBlocking {
        // Mockk
        val localKaizenSports = createMockKaizenSports()
        coEvery { sportsEventsLocalDataSource.getAllSportsEvents() } returns localKaizenSports

        // Run test
        val result = sportsEventsRepositoryImpl.getAllSportsFromDataBase()

        // Assert
        assertEquals(localKaizenSports, result)
        coVerify {
            sportsEventsLocalDataSource.getAllSportsEvents()
        }
    }

    @Test
    fun `getAllSportsEvents should return local data when local save fails`() = runBlocking {
        // Mockk
        val remoteKaizenSports = createMockKaizenSports()
        val localKaizenSports = createMockKaizenSports()
        coEvery { sportsEventsRemoteDataSource.getAllSportsEvents() } returns remoteKaizenSports
        coEvery { sportsEventsLocalDataSource.getAllSportsEvents() } returns localKaizenSports
        coEvery { sportsEventsLocalDataSource.saveSportsIntoDatabase(any()) } returns false

        // Run test
        val result = sportsEventsRepositoryImpl.getAllSportsEvents()

        // Assert
        assertEquals(localKaizenSports, result)
        coVerify {
            sportsEventsRemoteDataSource.getAllSportsEvents()
            sportsEventsLocalDataSource.getAllSportsEvents()
            sportsEventsLocalDataSource.saveSportsIntoDatabase(any())
        }
    }

    private fun createMockKaizenSports(): KaizenSports {
        return KaizenSports(
            activeSports = listOf(createMockkSportDetailObject())
        )
    }

    private fun createMockkSportDetailObject(): SportDetail {
        return SportDetail(
            eventId = EVENT_ID_INT,
            sportId = SPORT_ID,
            sportName = SPORT_NAME,
            activeSportEvents = listOf(createMockkEventDetailObject())
        )
    }

    private fun createMockkEventDetailObject(): EventDetail {
        return EventDetail(
            eventId = EVENT_ID,
            sportId = SPORT_ID,
            eventName = EVENT_NAME,
            eventStartTime = EVENT_START_TIME,
            isEventFavorite = false
        )
    }

    companion object {
        const val SPORT_ID = "sport_id"
        const val SPORT_NAME = "sport_name"
        const val EVENT_ID = "event_id"
        const val EVENT_ID_INT = 1
        const val EVENT_NAME = "event_name"
        const val EVENT_START_TIME = 1234
    }
}