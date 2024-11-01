package com.joao.otavio.kaizen.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val borderDefault: Dp = 1.dp,
    val spaceSExtraSmall: Dp = 2.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceSMedium: Dp = 12.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceSLMedium: Dp = 24.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceSLarge: Dp = 56.dp,
    val spaceExtraLarge: Dp = 64.dp,
)

data class ComponentsDimensions(
    val eventsLazyGridMinSize: Dp = 128.dp,
    val eventsLazyGridHeight: Dp = 300.dp,
    val eventsEmptyStateIconSize: Dp = 150.dp
)

val LocalComponentsSpacing = compositionLocalOf { ComponentsDimensions() }

val LocalSpacing = compositionLocalOf { Dimensions() }
