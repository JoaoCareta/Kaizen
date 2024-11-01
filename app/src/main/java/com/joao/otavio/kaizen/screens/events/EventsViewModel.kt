package com.joao.otavio.kaizen.screens.events

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao.otavio.kaizen.data.repositories.favoriteevents.FavoriteEventsRepository
import com.joao.otavio.kaizen.data.repositories.sportsevents.SportsEventsRepository
import com.joao.otavio.kaizen.domain.models.actions.EventsScreenActions
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.events.SportDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val sportsEventsRepository: SportsEventsRepository,
    private val favoriteEventsRepository: FavoriteEventsRepository,
) : ViewModel() {

    private val _allSportsEvents: MutableState<KaizenSports> =
        mutableStateOf(KaizenSports(emptyList()))
    val allSportsEvents: State<KaizenSports> = _allSportsEvents
    private val _loadingDataState: MutableState<Boolean> = mutableStateOf(false)
    val loadingDataState: State<Boolean> = _loadingDataState

    fun onEventScreenEvent(eventsScreenActions: EventsScreenActions) {
        when (eventsScreenActions) {
            is EventsScreenActions.GetAllSportsEvents -> handleGetAllEvents()
            is EventsScreenActions.OnFavoriteSportClick -> handleOnFavoriteSportClick(
                eventsScreenActions.eventDetail
            )

            is EventsScreenActions.OnFavoriteToggleFilterClick -> handleOnFavoriteToggleFilterClick(
                eventsScreenActions.sportDetail
            )

            is EventsScreenActions.OnSportExpandCollapse -> handleOnSportExpandCollapse(
                eventsScreenActions.sportDetail
            )
        }
    }

    private fun handleGetAllEvents() {
        _loadingDataState.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _allSportsEvents.value = sportsEventsRepository.getAllSportsEvents()
            _loadingDataState.value = false
        }
    }

    private fun handleOnFavoriteSportClick(eventDetail: EventDetail) {
        viewModelScope.launch {
            val success = handleFavoriteEventsDataBase(eventDetail)
            if (success) {
                updateAllSportsEvents(eventDetail)
            }
        }
    }

    private suspend fun handleFavoriteEventsDataBase(eventDetail: EventDetail): Boolean {
        return withContext(Dispatchers.IO) {
            if (!eventDetail.isEventFavorite) {
                favoriteEventsRepository.saveFavoriteEvent(eventDetail)
            } else {
                favoriteEventsRepository.deleteFavoriteEvent(eventDetail)
            }
        }
    }

    private fun updateAllSportsEvents(eventDetail: EventDetail) {
        _allSportsEvents.value = _allSportsEvents.value.copy(
            activeSports = _allSportsEvents.value.activeSports.map { sportDetail ->
                if (sportDetail.activeSportEvents.any { it.eventId == eventDetail.eventId }) {
                    val updatedEvents = sportDetail.activeSportEvents.map { event ->
                        if (event.eventId == eventDetail.eventId) {
                            event.copy(isEventFavorite = !event.isEventFavorite)
                        } else {
                            event
                        }
                    }

                    val finalEvents = if (sportDetail.filterFavorites) {
                        updatedEvents.filter { it.isEventFavorite }
                    } else {
                        updatedEvents
                    }

                    sportDetail.copy(activeSportEvents = finalEvents)
                } else {
                    sportDetail
                }
            }
        )
    }

    private fun handleOnFavoriteToggleFilterClick(clickedSportDetail: SportDetail) {
        viewModelScope.launch {
            _allSportsEvents.value = _allSportsEvents.value.copy(
                activeSports = _allSportsEvents.value.activeSports.map { sportDetail ->
                    if (sportDetail.sportId == clickedSportDetail.sportId) {
                        val newFilterFavorites = !sportDetail.filterFavorites

                        val updatedEventsForSport = getAllEventsForSport(sportDetail.sportId, newFilterFavorites)

                        sportDetail.copy(
                            filterFavorites = newFilterFavorites,
                            activeSportEvents = updatedEventsForSport
                        )
                    } else {
                        sportDetail
                    }
                }
            )
        }
    }

    private suspend fun getAllEventsForSport(sportId: String, filterFavorites: Boolean): List<EventDetail> {
        return if (filterFavorites) {
            favoriteEventsRepository.getFavoriteEventsBySportId(sportId)
        } else {
            val allEvents = sportsEventsRepository.getAllSportsFromDataBase()
            _allSportsEvents.value = allEvents

            val currentFavorites = favoriteEventsRepository.getFavoriteEventsBySportId(sportId)
                .map { it.eventId }
                .toSet()

            allEvents.activeSports
                .find { it.sportId == sportId }
                ?.activeSportEvents
                ?.map { event ->
                    event.copy(isEventFavorite = currentFavorites.contains(event.eventId))
                } ?: emptyList()
        }
    }


    private fun handleOnSportExpandCollapse(clickedSportDetail: SportDetail) {
        _allSportsEvents.value = _allSportsEvents.value.copy(
            activeSports = _allSportsEvents.value.activeSports.map { sportDetail ->
                if (sportDetail.sportId == clickedSportDetail.sportId) {
                    sportDetail.copy(isExpanded = !sportDetail.isExpanded)
                } else {
                    sportDetail
                }
            }
        )
    }

}
