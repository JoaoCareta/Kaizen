package com.joao.otavio.kaizen.screens.events.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joao.otavio.kaizen.R
import com.joao.otavio.kaizen.domain.models.events.EventDetail
import com.joao.otavio.kaizen.domain.models.events.SportDetail
import com.joao.otavio.kaizen.screens.events.components.EventsScreenComponents.DELIMITER
import com.joao.otavio.kaizen.screens.events.components.EventsScreenComponents.MINUTES
import com.joao.otavio.kaizen.screens.events.components.EventsScreenComponents.ONE_SECOND_IN_MILLIS
import com.joao.otavio.kaizen.screens.events.components.EventsScreenComponents.SECONDS
import com.joao.otavio.kaizen.ui.theme.Blue40
import com.joao.otavio.kaizen.ui.theme.Coral40
import com.joao.otavio.kaizen.ui.theme.Gray40
import com.joao.otavio.kaizen.ui.theme.Orange40
import com.joao.otavio.kaizen.utils.LocalComponentsSpacing
import com.joao.otavio.kaizen.utils.LocalSpacing
import com.joao.otavio.kaizen.utils.TimeUtils.getCurrentTimeInSeconds
import kotlinx.coroutines.delay
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun SportsList(
    activeSports: List<SportDetail>,
    onFavoriteClick: (EventDetail) -> Unit,
    onToggleFavoriteFilter: (SportDetail) -> Unit,
    onExpandCollapse: (SportDetail) -> Unit
) {
    val localSpacing = LocalSpacing.current
    LazyColumn(
        modifier = Modifier.background(Gray40),
        verticalArrangement = Arrangement.spacedBy(localSpacing.spaceMedium)
    ) {
        items(activeSports) { sportDetail ->
            SportCategoryItem(
                sportDetail = sportDetail,
                onToggleFavoriteFilter = { onToggleFavoriteFilter(sportDetail) },
                onExpandCollapse = { onExpandCollapse(sportDetail) },
                onFavoriteClick = { eventDetail ->
                    onFavoriteClick(eventDetail)
                }
            )
        }
    }
}

@Composable
fun SportCategoryItem(
    sportDetail: SportDetail,
    onToggleFavoriteFilter: () -> Unit,
    onExpandCollapse: (SportDetail) -> Unit,
    onFavoriteClick: (EventDetail) -> Unit
) {
    val localSpacing = LocalSpacing.current
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(localSpacing.spaceSmall),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(localSpacing.spaceSmall)
                .clickable { onExpandCollapse(sportDetail) }
        ) {
            Canvas(
                modifier = Modifier
                    .size(localSpacing.spaceMedium),
            ) {
                drawCircle(
                    color = Coral40
                )
            }
            Text(
                text = sportDetail.sportName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = Gray40
            )

            Switch(
                checked = sportDetail.filterFavorites,
                onCheckedChange = {
                    onToggleFavoriteFilter.invoke()
                },
                thumbContent = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = stringResource(R.string.star_icon),
                        tint = Color.White
                    )
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Gray40,
                    checkedThumbColor = Blue40,
                    uncheckedTrackColor = Color.LightGray,
                    checkedTrackColor = Color.Black
                )
            )

            IconButton(onClick = { onExpandCollapse.invoke(sportDetail) }) {
                Icon(
                    imageVector = if (sportDetail.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.expand_collapse),
                    tint = Gray40
                )
            }
        }

        if (sportDetail.isExpanded) {
            if (sportDetail.activeSportEvents.isEmpty()) {
                EmptySportsEventsContent()
            } else {
                GamesGridView(
                    gamesList = sportDetail.activeSportEvents,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun GamesGridView(
    gamesList: List<EventDetail>,
    onFavoriteClick: (EventDetail) -> Unit
) {
    val localSpacing = LocalSpacing.current
    val componentsSpacing = LocalComponentsSpacing.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = componentsSpacing.eventsLazyGridMinSize),
        contentPadding = PaddingValues(horizontal = localSpacing.spaceMedium, vertical = localSpacing.spaceSmall),
        verticalArrangement = Arrangement.spacedBy(localSpacing.spaceSMedium),
        horizontalArrangement = Arrangement.spacedBy(localSpacing.spaceSmall),
        modifier = Modifier
            .background(Gray40)
            .height(componentsSpacing.eventsLazyGridHeight)
    ) {
        items(gamesList) { game ->
            GameItem(
                eventDetail = game,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun GameItem(
    eventDetail: EventDetail,
    onFavoriteClick: (EventDetail) -> Unit
) {
    val localSpacing = LocalSpacing.current
    val currentTime by produceState(initialValue = getCurrentTimeInSeconds() ) {
        while (true) {
            delay(ONE_SECOND_IN_MILLIS)
            value = getCurrentTimeInSeconds()
        }
    }

    val remainingTime by remember(currentTime) {
        derivedStateOf {
            (eventDetail.eventStartTime.toLong()) - currentTime
        }
    }

    Column(
        modifier = Modifier
            .padding(localSpacing.spaceExtraSmall)
            .background(Gray40),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(localSpacing.spaceSExtraSmall)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Gray40,
                    shape = RoundedCornerShape(localSpacing.spaceExtraSmall)
                )
                .border(
                    width = localSpacing.borderDefault,
                    color = Blue40,
                    shape = RoundedCornerShape(localSpacing.spaceExtraSmall)
                )
                .padding(
                    horizontal = localSpacing.spaceSmall,
                    vertical = localSpacing.spaceExtraSmall
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formatTime(remainingTime),
                color = Color.White
            )
        }
        IconButton(onClick = { onFavoriteClick(eventDetail) }) {
            Icon(
                imageVector = if (eventDetail.isEventFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = stringResource(R.string.star_favorite_switch_button),
                tint = if (eventDetail.isEventFavorite) Orange40 else Color.Black
            )
        }
        val competitors = eventDetail.eventName.split(DELIMITER)
        Text(
            text = competitors.first(),
            color = Color.White
        )
        Text(
            text = stringResource(R.string.vs),
            color = Coral40,
        )
        Text(
            text = competitors.last(),
            color = Color.White
        )
    }
}

@Composable
fun EmptySportsEventsContent() {
    val componentsSpacing = LocalComponentsSpacing.current
    Column(
        modifier = Modifier
            .height(componentsSpacing.eventsLazyGridHeight)
            .fillMaxWidth()
            .background(Gray40),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_sentiment_dissatisfied_24),
            contentDescription = stringResource(R.string.no_events_to_display_icon),
            tint = Color.LightGray,
            modifier = Modifier
                .size(componentsSpacing.eventsEmptyStateIconSize)
        )
        Text(
            text = stringResource(R.string.empty_events_message),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
        )
    }
}

@Composable
@Preview
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray40),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.loading),
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
    }
}

@Composable
@Preview
fun EmptySportsContent() {
    val componentsSpacing = LocalComponentsSpacing.current
    val localSpacing = LocalSpacing.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray40),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(localSpacing.spaceSmall)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_sentiment_dissatisfied_24),
                contentDescription = stringResource(R.string.no_events_to_display_icon),
                tint = Color.LightGray,
                modifier = Modifier
                    .size(componentsSpacing.eventsEmptyStateIconSize)
            )
            Spacer(modifier = Modifier.height(localSpacing.spaceMedium))
            Text(
                text = "Sorry, there are no Sports events to display at moment",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun formatTime(remainingTimeMillis: Long): String {
    if (remainingTimeMillis <= 0) return stringResource(R.string.final_timer)

    val hours = TimeUnit.SECONDS.toHours(remainingTimeMillis)
    val minutes = TimeUnit.SECONDS.toMinutes(remainingTimeMillis) % MINUTES
    val seconds = remainingTimeMillis % SECONDS

    return String.format(Locale.US, stringResource(R.string.timer_template), hours, minutes, seconds)
}

object EventsScreenComponents {
    const val DELIMITER = "-"
    const val MINUTES = 60
    const val SECONDS = 60
    const val ONE_SECOND_IN_MILLIS = 1000L
}

@Composable
@Preview(showBackground = true)
fun GameItemPreview(modifier: Modifier = Modifier) {
    val eventDetail = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1730437545,
        isEventFavorite = false,
    )
    GameItem(eventDetail = eventDetail) { }
}

@Composable
@Preview(showBackground = true)
fun SportCategoryItemPreview(modifier: Modifier = Modifier) {
    val eventDetail_1 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_2 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_3 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_4 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_5 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_6 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_7 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_8 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_9 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )

    val sportDetail = SportDetail(
        sportId = "FOOT",
        sportName = "SOCCER",
        activeSportEvents = listOf(
            eventDetail_1,
            eventDetail_2,
            eventDetail_3,
            eventDetail_4,
            eventDetail_5,
            eventDetail_6,
            eventDetail_7,
            eventDetail_8,
            eventDetail_9
        )
    )

    SportCategoryItem(
        sportDetail = sportDetail,
        onToggleFavoriteFilter = {},
        onExpandCollapse = {},
        onFavoriteClick = { _ ->
        }
    )

}

@Composable
@Preview(showBackground = true)
fun SportCategoryEmptyItemPreview(modifier: Modifier = Modifier) {

    val sportDetail = SportDetail(
        sportId = "FOOT",
        sportName = "SOCCER",
        activeSportEvents = emptyList()
    )

    SportCategoryItem(
        sportDetail = sportDetail,
        onToggleFavoriteFilter = {},
        onExpandCollapse = {},
        onFavoriteClick = { _ ->
        }
    )

}


@Composable
@Preview(showBackground = true)
fun SportsListPreview(modifier: Modifier = Modifier) {
    val eventDetail_1 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_2 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_3 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_4 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_5 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_6 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_7 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_8 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )
    val eventDetail_9 = EventDetail(
        eventId = "22911144",
        sportId = "FOOT",
        eventName = "Greece-Spain",
        eventStartTime = 1657944684,
        isEventFavorite = false,
    )

    val sportDetail = SportDetail(
        sportId = "FOOT",
        sportName = "SOCCER",
        activeSportEvents = listOf(
            eventDetail_1,
            eventDetail_2,
            eventDetail_3,
            eventDetail_4,
            eventDetail_5,
            eventDetail_6,
            eventDetail_7,
            eventDetail_8,
            eventDetail_9
        )
    )

    val sportDetail_2 = SportDetail(
        sportId = "FOOT",
        sportName = "FOOTBALL",
        activeSportEvents = emptyList()
    )

    val sportDetail_3 = SportDetail(
        sportId = "FOOT",
        sportName = "BASKETBALL",
        activeSportEvents = listOf(
            eventDetail_1,
            eventDetail_2,
            eventDetail_3,
            eventDetail_4,
            eventDetail_5,
            eventDetail_6,
            eventDetail_7,
            eventDetail_8,
            eventDetail_9
        )
    )

    val sportDetail_4 = SportDetail(
        sportId = "FOOT",
        sportName = "ESPORTS",
        activeSportEvents = listOf(
            eventDetail_1,
            eventDetail_2,
            eventDetail_3,
            eventDetail_4,
            eventDetail_5,
            eventDetail_6,
            eventDetail_7,
            eventDetail_8,
            eventDetail_9
        )
    )


    val activeSports = listOf(sportDetail, sportDetail_2, sportDetail_3, sportDetail_4)

    SportsList(
        activeSports,
        { _ ->

        },

        { _ ->

        },

        {

        }
    )
}

