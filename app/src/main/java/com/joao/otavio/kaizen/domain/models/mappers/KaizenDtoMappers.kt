package com.joao.otavio.kaizen.domain.models.mappers

import com.joao.otavio.kaizen.domain.models.dto.E
import com.joao.otavio.kaizen.domain.models.dto.KaizenApiDto
import com.joao.otavio.kaizen.domain.models.dto.KaizenApiDtoItem
import com.joao.otavio.kaizen.domain.models.entities.favoriteevents.FavoriteEvents
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.events.KaizenSports
import com.joao.otavio.kaizen.domain.models.events.SportDetail
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.KaizenSports as KaizenSportsEntity
import com.joao.otavio.kaizen.domain.models.entities.kaizensports.EventDetail as EventDetailEntity

// Region DTO --> Domain
fun E.toEventDetail(): EventDetail {
    return EventDetail(
        eventId = this.i,
        sportId = this.si,
        eventName = this.d,
        eventStartTime = this.tt
    )
}

fun KaizenApiDtoItem.toSportDetail(): SportDetail {
    return SportDetail(
        sportId = this.i,
        sportName = this.d,
        activeSportEvents = this.e.map { it.toEventDetail() }
    )
}

fun KaizenApiDto.toKaizenSports(): KaizenSports {
    return KaizenSports(
        activeSports = this.map { it.toSportDetail() }
    )
}
// End region

// Region Domain --> Entity
fun KaizenSports.toEntity(): List<KaizenSportsEntity> {
    return this.activeSports.map { it.toEntity() }
}

fun SportDetail.toEntity(): KaizenSportsEntity {
    return KaizenSportsEntity(
        sportId = this.sportId,
        sportName = this.sportName,
        activeSportEvents = this.activeSportEvents.map { it.toEntity() }
    )
}

fun EventDetail.toEntity(): EventDetailEntity {
    return EventDetailEntity(
        eventId = this.eventId,
        sportId = this.sportId,
        eventName = this.eventName,
        eventStartTime = this.eventStartTime,
        isEventFavorite = this.isEventFavorite
    )
}
// End region

// Region Entity --> Domain
fun List<KaizenSportsEntity>.toDomain(): KaizenSports {
    return KaizenSports(
        activeSports = this.map { it.toDomain() }
    )
}

fun KaizenSportsEntity.toDomain(): SportDetail {
    return SportDetail(
        eventId = this.eventId,
        sportId = this.sportId,
        sportName = this.sportName,
        activeSportEvents = this.activeSportEvents.map { it.toDomain() },
    )
}

fun EventDetailEntity.toDomain(): EventDetail {
    return EventDetail(
        eventId = this.eventId,
        sportId = this.sportId,
        eventName = this.eventName,
        eventStartTime = this.eventStartTime,
        isEventFavorite = this.isEventFavorite
    )
}
// End region

// Region EventDetail --> FavoriteEvents
fun EventDetail.toFavoriteEvents(): FavoriteEvents {
    return FavoriteEvents(
        eventId = this.eventId,
        sportId = this.sportId,
        eventName = this.eventName,
        eventStartTime = this.eventStartTime
    )
}
// End region

// Region FavoriteEvents --> EventDetail
fun FavoriteEvents.toEventDetail(): EventDetail {
    return EventDetail(
        eventId = this.eventId,
        sportId = this.sportId,
        eventName = this.eventName,
        eventStartTime = this.eventStartTime,
        isEventFavorite = true
    )
}
// End region

