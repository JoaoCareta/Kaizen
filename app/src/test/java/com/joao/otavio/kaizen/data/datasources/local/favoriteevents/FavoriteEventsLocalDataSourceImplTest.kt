package com.joao.otavio.kaizen.data.datasources.local.favoriteevents

import android.util.Log
import com.joao.otavio.kaizen.data.local.dao.FavoriteEventsDao
import com.joao.otavio.kaizen.domain.models.entities.favoriteevents.FavoriteEvents
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.mappers.toEventDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteEventsLocalDataSourceImplTest {
    private val favoriteEventsDao: FavoriteEventsDao = mockk()

    private val favoriteEventsLocalDataSourceImpl = FavoriteEventsLocalDataSourceImpl(
        favoriteEventsDao
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        confirmVerified(favoriteEventsDao)
    }

    @Test
    fun `given a sportId, when dao is able to getFavoriteEventsBySportId, then it should return a listOf EventDetail`() = runBlocking {
        // Mockk
        val mockkListOfFavoriteEvents = listOf(createEventDetailObject())
        val expected = mockkListOfFavoriteEvents.map { it.toEventDetail() }
        coEvery { favoriteEventsDao.getFavoriteEventsBySportId(SPORT_ID) } returns mockkListOfFavoriteEvents

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.getFavoriteEventsBySportId(SPORT_ID)

        // Assert
        assertEquals(expected, result)
        coVerify {
            favoriteEventsDao.getFavoriteEventsBySportId(SPORT_ID)
        }
    }

    @Test
    fun `given a sportId, when dao is not able to getFavoriteEventsBySportId, then it should return an emptyListOf EventDetail`() = runBlocking {
        // Mockk
        coEvery { favoriteEventsDao.getFavoriteEventsBySportId(SPORT_ID) } returns emptyList()

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.getFavoriteEventsBySportId(SPORT_ID)

        // Assert
        assertEquals(emptyList<EventDetail>(), result)
        coVerify {
            favoriteEventsDao.getFavoriteEventsBySportId(SPORT_ID)
        }
    }

    @Test
    fun `given a sportId, when dao fails to getFavoriteEventsBySportId and throws and Exception, then it should return an emptyListOf EventDetail`() = runBlocking {
        // Mockk
        coEvery { favoriteEventsDao.getFavoriteEventsBySportId(SPORT_ID) } throws  Exception()

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.getFavoriteEventsBySportId(SPORT_ID)

        // Assert
        assertEquals(emptyList<EventDetail>(), result)
        coVerify {
            favoriteEventsDao.getFavoriteEventsBySportId(SPORT_ID)
        }
    }

    @Test
    fun `given a sportId, when dao is able to saveFavoriteEvent, then it should return true`() = runBlocking {
        // Mockk
        val mockkFavoriteEvents = createEventDetailObject()
        coEvery { favoriteEventsDao.upsert(mockkFavoriteEvents) } returns true

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.saveFavoriteEvent(mockkFavoriteEvents)

        // Assert
        assertTrue(result)
        coVerify {
            favoriteEventsDao.upsert(mockkFavoriteEvents)
        }
    }

    @Test
    fun `given a sportId, when dao is not able to saveFavoriteEvent, then it should return false`() = runBlocking {
        // Mockk
        val mockkFavoriteEvents = createEventDetailObject()
        coEvery { favoriteEventsDao.upsert(mockkFavoriteEvents) } returns false

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.saveFavoriteEvent(mockkFavoriteEvents)

        // Assert
        assertFalse(result)
        coVerify {
            favoriteEventsDao.upsert(mockkFavoriteEvents)
        }
    }

    @Test
    fun `given a sportId, when dao fails to saveFavoriteEvent and throws and Exception, then it should return false`() = runBlocking {
        // Mockk
        val mockkFavoriteEvents = createEventDetailObject()
        coEvery { favoriteEventsDao.upsert(mockkFavoriteEvents) } throws Exception()

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.saveFavoriteEvent(mockkFavoriteEvents)

        // Assert
        assertFalse(result)
        coVerify {
            favoriteEventsDao.upsert(mockkFavoriteEvents)
        }
    }

    @Test
    fun `given a sportId, when dao is able to deleteFavoriteEvent, then it should return true`() = runBlocking {
        // Mockk
        val mockkFavoriteEvents = createEventDetailObject()
        justRun { favoriteEventsDao.delete(mockkFavoriteEvents) }

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.deleteFavoriteEvent(mockkFavoriteEvents)

        // Assert
        assertTrue(result)
        coVerify {
            favoriteEventsDao.delete(mockkFavoriteEvents)
        }
    }

    @Test
    fun `given a sportId, when dao fails to deleteFavoriteEvent and throws and Exception, then it should return false`() = runBlocking {
        // Mockk
        val mockkFavoriteEvents = createEventDetailObject()
        coEvery { favoriteEventsDao.delete(mockkFavoriteEvents) } throws Exception()

        // Run test
        val result = favoriteEventsLocalDataSourceImpl.deleteFavoriteEvent(mockkFavoriteEvents)

        // Assert
        assertFalse(result)
        coVerify {
            favoriteEventsDao.delete(mockkFavoriteEvents)
        }
    }

    private fun createEventDetailObject(): FavoriteEvents {
        return FavoriteEvents(
            eventId = EVENT_ID,
            sportId = SPORT_ID,
            eventName = EVENT_NAME,
            eventStartTime = EVENT_START_TIME,
        )
    }

    companion object {
        const val EVENT_ID = "event_id"
        const val SPORT_ID = "event_id"
        const val EVENT_NAME = "event_id"
        const val EVENT_START_TIME = 1
    }
}
