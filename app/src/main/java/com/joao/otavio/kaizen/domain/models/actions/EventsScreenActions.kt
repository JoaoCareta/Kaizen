package com.joao.otavio.kaizen.domain.models.actions

import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.events.SportDetail

sealed class EventsScreenActions {
    data object GetAllSportsEvents: EventsScreenActions()
    data class OnFavoriteSportClick(val eventDetail: EventDetail): EventsScreenActions()
    data class OnFavoriteToggleFilterClick(val sportDetail: SportDetail): EventsScreenActions()
    data class OnSportExpandCollapse(val sportDetail: SportDetail): EventsScreenActions()
}
