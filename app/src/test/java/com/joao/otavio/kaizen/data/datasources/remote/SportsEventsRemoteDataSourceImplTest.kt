package com.joao.otavio.kaizen.data.datasources.remote

import android.util.Log
import com.joao.otavio.kaizen.data.remote.EventsApi
import com.joao.otavio.kaizen.domain.models.dto.KaizenApiDto
import com.joao.otavio.kaizen.domain.models.dto.KaizenApiDtoItem
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.mappers.toKaizenSports
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class SportsEventsRemoteDataSourceImplTest {

    private val eventsApi: EventsApi = mockk()
    private lateinit var sportsEventsRemoteDataSourceImpl: SportsEventsRemoteDataSourceImpl

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        sportsEventsRemoteDataSourceImpl = SportsEventsRemoteDataSourceImpl(eventsApi)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        confirmVerified(eventsApi)
    }

    @Test
    fun `given API returns data, when getAllSportsEvents is called, then it should return KaizenSports with data`() = runBlocking {
        // Mockk
        val mockSportResponse = createMockSportResponse()
        coEvery { eventsApi.getSportsEvents() } returns mockSportResponse

        // Run Test
        val result = sportsEventsRemoteDataSourceImpl.getAllSportsEvents()

        // Assert
        assertEquals(mockSportResponse.toKaizenSports(), result)
        coVerify { eventsApi.getSportsEvents() }
    }

    @Test
    fun `given API returns empty list, when getAllSportsEvents is called, then it should return KaizenSports with empty list`() = runBlocking {
        // Mockk
        coEvery { eventsApi.getSportsEvents() } returns KaizenApiDto()

        // Run Test
        val result = sportsEventsRemoteDataSourceImpl.getAllSportsEvents()

        // Assert
        assertEquals(KaizenSports(emptyList()), result)
        coVerify { eventsApi.getSportsEvents() }
    }

    @Test
    fun `given API throws exception, when getAllSportsEvents is called, then it should return KaizenSports with empty list and log error`() = runBlocking {
        // Mockk
        val exception = HttpException(mockk(relaxed = true))
        coEvery { eventsApi.getSportsEvents() } throws exception

        // Run Test
        val result = sportsEventsRemoteDataSourceImpl.getAllSportsEvents()

        // Assert
        assertEquals(KaizenSports(emptyList()), result)
        coVerify {
            eventsApi.getSportsEvents()
            Log.e(any(), any())
        }
    }

    private fun createMockSportResponse(): KaizenApiDto {
        return KaizenApiDto().apply {
            add(createMockKaizenApiDtoItem())
        }
    }

    private fun createMockKaizenApiDtoItem(): KaizenApiDtoItem {
        return KaizenApiDtoItem(
            d = EVENT_NAME,
            e = listOf(),
            i = EVENT_ID,
        )
    }

    companion object {
        const val EVENT_ID = "event_id"
        const val EVENT_NAME = "event_name"
    }
}