package com.xbot.designsystem.components

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.designsystem.icons.AcUnit
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Eco
import com.xbot.designsystem.icons.LocalFlorist
import com.xbot.designsystem.icons.Movie
import com.xbot.designsystem.icons.SmartDisplay
import com.xbot.designsystem.icons.Sunny
import com.xbot.designsystem.icons.Tv
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season

val Season.icon: ImageVector
    get() = when (this) {
        Season.WINTER -> AnilibertyIcons.AcUnit
        Season.SPRING -> AnilibertyIcons.LocalFlorist
        Season.SUMMER -> AnilibertyIcons.Sunny
        Season.AUTUMN -> AnilibertyIcons.Eco
    }

val ReleaseType.icon: ImageVector
    get() = when (this) {
        ReleaseType.TV -> AnilibertyIcons.Tv
        ReleaseType.MOVIE -> AnilibertyIcons.Movie
        else -> AnilibertyIcons.SmartDisplay
    }
