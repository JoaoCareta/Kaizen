package com.joao.otavio.kaizen.data.datasources.local.sportsevents

import android.util.Log
import com.joao.otavio.kaizen.data.local.dao.KaizenSportsDao
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.EventDetail as EventDetailEntity
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.mappers.toDomain
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.KaizenSports as KaizenSportsEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SportsEventsLocalDataSourceImplTest {

    private val kaizenSportsDao: KaizenSportsDao = mockk<KaizenSportsDao>()
    private var sportsEventsLocalDataSourceImpl = SportsEventsLocalDataSourceImpl(kaizenSportsDao)

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        confirmVerified(kaizenSportsDao)
    }

    @Test
    fun `given a listOf sportsEventsEntity, when dao does not fail, then it should return true`() = runBlocking {
        // Mockk
        val mockkKaizenSportsEntity = createKaizenSportsEntityObject()
        coEvery { kaizenSportsDao.deleteAllSports() } returns 1
        coEvery { kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity)) } returns true

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.saveSportsIntoDatabase(listOf(mockkKaizenSportsEntity))

        // Assert
        assertTrue(result)
        coVerify {
            kaizenSportsDao.deleteAllSports()
            kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity))
        }
    }

    @Test
    fun `given a listOf sportsEventEntity, when dao fails to upsertAll, then it should return false`() = runBlocking {
        // Mockk
        val mockkKaizenSportsEntity = createKaizenSportsEntityObject()
        coEvery { kaizenSportsDao.deleteAllSports() } returns 1
        coEvery { kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity)) } returns false

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.saveSportsIntoDatabase(listOf(mockkKaizenSportsEntity))

        // Assert
        assertFalse(result)
        coVerify {
            kaizenSportsDao.deleteAllSports()
            kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity))
        }
    }

    @Test
    fun `given a listOf sportsEventEntity, when dao fails to delete the previous sports, then it should return false`() = runBlocking {
        // Mockk
        val mockkKaizenSportsEntity = createKaizenSportsEntityObject()
        coEvery { kaizenSportsDao.deleteAllSports() } returns -1

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.saveSportsIntoDatabase(listOf(mockkKaizenSportsEntity))

        // Assert
        assertFalse(result)
        coVerify(exactly = 0) { kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity)) }
        coVerify {
            kaizenSportsDao.deleteAllSports()
            Log.e(any(), any())
        }
    }

    @Test
    fun `given a listOf sportsEventEntity, when dao fails to upsertAll throwing an exception, then it should return false`() = runBlocking {
        // Mockk
        val mockkKaizenSportsEntity = createKaizenSportsEntityObject()
        coEvery { kaizenSportsDao.deleteAllSports() } returns 1
        coEvery { kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity)) } throws Exception()

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.saveSportsIntoDatabase(listOf(mockkKaizenSportsEntity))

        // Assert
        assertFalse(result)
        coVerify {
            kaizenSportsDao.deleteAllSports()
            kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity))
            Log.e(any(), any())
        }
    }

    @Test
    fun `given a listOf sportsEventEntity, when dao fails throwing an exception, then it should return false`() = runBlocking {
        // Mockk
        val mockkKaizenSportsEntity = createKaizenSportsEntityObject()
        coEvery { kaizenSportsDao.deleteAllSports() } throws Exception()

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.saveSportsIntoDatabase(listOf(mockkKaizenSportsEntity))

        // Assert
        assertFalse(result)
        coVerify(exactly = 0) { kaizenSportsDao.upsertAll(listOf(mockkKaizenSportsEntity)) }
        coVerify {
            kaizenSportsDao.deleteAllSports()
            Log.e(any(), any())
        }
    }

    @Test
    fun `given getAllSports returns data, when getAllSportsEvents is called, then it should return KaizenSports with data`() = runBlocking {
        // Mockk
        val mockkKaizenSportsEntityList = listOf(createKaizenSportsEntityObject())
        coEvery { kaizenSportsDao.getAllSports() } returns mockkKaizenSportsEntityList

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.getAllSportsEvents()

        // Assert
        assertEquals(mockkKaizenSportsEntityList.toDomain(), result)
        coVerify { kaizenSportsDao.getAllSports() }
    }

    @Test
    fun `given getAllSports returns empty list, when getAllSportsEvents is called, then it should return KaizenSports with empty list`() = runBlocking {
        // Mockk
        coEvery { kaizenSportsDao.getAllSports() } returns emptyList()

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.getAllSportsEvents()

        // Assert
        assertEquals(KaizenSports(emptyList()), result)
        coVerify { kaizenSportsDao.getAllSports() }
    }

    @Test
    fun `given getAllSports throws exception, when getAllSportsEvents is called, then it should return KaizenSports with empty list and log error`() = runBlocking {
        // Mockk
        coEvery { kaizenSportsDao.getAllSports() } throws Exception()

        // Run Test
        val result = sportsEventsLocalDataSourceImpl.getAllSportsEvents()

        // Assert
        assertEquals(KaizenSports(emptyList()), result)
        coVerify {
            kaizenSportsDao.getAllSports()
            Log.e(any(), any())
        }
    }

    private fun createKaizenSportsEntityObject(): KaizenSportsEntity {
        return KaizenSportsEntity(
            sportId = SPORT_ID,
            sportName = SPORT_NAME,
            activeSportEvents = createListOfEventDetailEntity()
        )
    }

    private fun createListOfEventDetailEntity(): List<EventDetailEntity> {
        val eventDetailEntity = EventDetailEntity(
            eventId = EVENT_ID,
            sportId = SPORT_ID,
            eventName = EVENT_NAME,
            eventStartTime = EVENT_START_TIME,
            isEventFavorite = false
        )
        return listOf(eventDetailEntity)
    }

    companion object {
        const val EVENT_ID = "event_id"
        const val SPORT_ID = "sport_id"
        const val SPORT_NAME = "sport_name"
        const val EVENT_NAME = "event_name"
        const val EVENT_START_TIME = 1
    }
}