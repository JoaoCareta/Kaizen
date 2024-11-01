package com.joao.otavio.kaizen.data.repositories.favoriteevents

import com.joao.otavio.kaizen.data.datasources.local.favoriteevents.FavoriteEventsLocalDataSource
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.mappers.toFavoriteEvents
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteEventsRepositoryImplTest {

    private val favoriteEventsLocalDataSource: FavoriteEventsLocalDataSource = mockk()
    private val favoriteEventsRepositoryImpl = FavoriteEventsRepositoryImpl(
        favoriteEventsLocalDataSource
    )

    @After
    fun tearDown() {
        confirmVerified(favoriteEventsLocalDataSource)
    }

    @Test
    fun `given a sportId, repository should return a listOf favorites EventDetails`() = runBlocking {
        // Mockk
        val eventDetailMockkObject = createEventDetailObject()
        val expected = listOf(eventDetailMockkObject)

        coEvery { favoriteEventsLocalDataSource.getFavoriteEventsBySportId(SPORT_ID) } returns expected

        // Run test
        val result = favoriteEventsRepositoryImpl.getFavoriteEventsBySportId(SPORT_ID)

        // Assert
        assertEquals(expected, result)
        coVerify {
            favoriteEventsLocalDataSource.getFavoriteEventsBySportId(SPORT_ID)
        }
    }

    @Test
    fun `given a eventDetail, when favoriteEventsLocalDataSource is able to save the event, then it should return true`() = runBlocking {
        // Mockk
        val eventDetailMockkObject = createEventDetailObject()

        coEvery { favoriteEventsLocalDataSource.saveFavoriteEvent(eventDetailMockkObject.toFavoriteEvents()) } returns true

        // Run test
        val result = favoriteEventsRepositoryImpl.saveFavoriteEvent(eventDetailMockkObject)

        // Assert
        assertTrue(result)
        coVerify {
            favoriteEventsLocalDataSource.saveFavoriteEvent(eventDetailMockkObject.toFavoriteEvents())
        }
    }

    @Test
    fun `given a eventDetail, when favoriteEventsLocalDataSource is not able to save the event, then it should return false`() = runBlocking {
        // Mockk
        val eventDetailMockkObject = createEventDetailObject()

        coEvery { favoriteEventsLocalDataSource.saveFavoriteEvent(eventDetailMockkObject.toFavoriteEvents()) } returns false

        // Run test
        val result = favoriteEventsRepositoryImpl.saveFavoriteEvent(eventDetailMockkObject)

        // Assert
        assertFalse(result)
        coVerify {
            favoriteEventsLocalDataSource.saveFavoriteEvent(eventDetailMockkObject.toFavoriteEvents())
        }
    }

    @Test
    fun `given a eventDetail, when favoriteEventsLocalDataSource is able to delete the event, then it should return true`() = runBlocking {
        // Mockk
        val eventDetailMockkObject = createEventDetailObject()

        coEvery { favoriteEventsLocalDataSource.deleteFavoriteEvent(eventDetailMockkObject.toFavoriteEvents()) } returns true

        // Run test
        val result = favoriteEventsRepositoryImpl.deleteFavoriteEvent(eventDetailMockkObject)

        // Assert
        assertTrue(result)
        coVerify {
            favoriteEventsLocalDataSource.deleteFavoriteEvent(eventDetailMockkObject.toFavoriteEvents())
        }
    }

    @Test
    fun `given a eventDetail, when favoriteEventsLocalDataSource is not able to delete the event, then it should return false`() = runBlocking {
        // Mockk
        val eventDetailMockkObject = createEventDetailObject()

        coEvery { favoriteEventsLocalDataSource.deleteFavoriteEvent(eventDetailMockkObject.toFavoriteEvents()) } returns false

        // Run test
        val result = favoriteEventsRepositoryImpl.deleteFavoriteEvent(eventDetailMockkObject)

        // Assert
        assertFalse(result)
        coVerify {
            favoriteEventsLocalDataSource.deleteFavoriteEvent(eventDetailMockkObject.toFavoriteEvents())
        }
    }

    private fun createEventDetailObject(): EventDetail {
        return EventDetail(
            eventId = EVENT_ID,
            sportId = SPORT_ID,
            eventName = EVENT_NAME,
            eventStartTime = EVENT_START_TIME,
            isEventFavorite = true
        )
    }

    companion object {
        const val EVENT_ID = "event_id"
        const val SPORT_ID = "event_id"
        const val EVENT_NAME = "event_id"
        const val EVENT_START_TIME = 1
    }
}
