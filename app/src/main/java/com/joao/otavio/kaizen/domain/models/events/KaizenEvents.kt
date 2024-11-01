package com.joao.otavio.kaizen.domain.models.events

data class KaizenSports(
    val activeSports: List<SportDetail>
)

data class SportDetail(
    val eventId: Int = 0,
    val sportId: String,
    val sportName: String,
    val activeSportEvents: List<EventDetail>,
    val filterFavorites: Boolean = false,
    val isExpanded: Boolean = true
)

data class EventDetail(
    val eventId: String,
    val sportId: String,
    val eventName: String,
    val eventStartTime: Int,
    var isEventFavorite: Boolean = false
)
