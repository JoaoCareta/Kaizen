package com.joao.otavio.kaizen.screens.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.joao.otavio.kaizen.domain.models.actions.EventsScreenActions
import com.joao.otavio.kaizen.screens.events.components.EmptySportsContent
import com.joao.otavio.kaizen.screens.events.components.LoadingScreen
import com.joao.otavio.kaizen.screens.events.components.SportsList

@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel = hiltViewModel()
) {
    val allSportsEvents = eventsViewModel.allSportsEvents.value
    val loadingState = eventsViewModel.loadingDataState.value

    LaunchedEffect(key1 = true) {
        eventsViewModel.onEventScreenEvent(EventsScreenActions.GetAllSportsEvents)
    }

    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when {
                loadingState -> LoadingScreen()
                allSportsEvents.activeSports.isEmpty() -> EmptySportsContent()
                else -> SportsList(
                    activeSports = allSportsEvents.activeSports,
                    onFavoriteClick = { eventDetail ->
                        eventsViewModel.onEventScreenEvent(EventsScreenActions.OnFavoriteSportClick(eventDetail))
                    },
                    onToggleFavoriteFilter = { sportDetail ->
                        eventsViewModel.onEventScreenEvent(EventsScreenActions.OnFavoriteToggleFilterClick(sportDetail))
                    },
                    onExpandCollapse = { sportDetail ->
                        eventsViewModel.onEventScreenEvent(EventsScreenActions.OnSportExpandCollapse(sportDetail))
                    }
                )
            }
        }
    }
}
