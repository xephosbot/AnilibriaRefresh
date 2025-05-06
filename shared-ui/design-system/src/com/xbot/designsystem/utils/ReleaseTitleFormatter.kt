package com.xbot.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.State
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.episode_abbreviation
import com.xbot.resources.minutes_abbreviation
import org.jetbrains.compose.resources.getString

@Composable
internal fun releaseTitleState(release: Release): State<String> {
    return produceState(initialValue = "", release) {
        value = buildReleaseTitle(release)
    }
}

private suspend fun buildReleaseTitle(release: Release): String = buildString {
    append(release.year.toString())
    append(" \u2022 ")
    release.type?.let { type ->
        when (type) {
            ReleaseType.MOVIE -> {
                append(getString(type.stringRes))
                append(" \u2022 ")
            }
            else -> {
                release.episodesCount?.let { episodesCount ->
                    append(getString(Res.string.episode_abbreviation, episodesCount.toString()))
                    append(" \u2022 ")
                }
            }
        }
    }
    release.episodeDuration?.let { episodeDuration ->
        append(getString(Res.string.minutes_abbreviation, episodeDuration.toString()))
        append(" \u2022 ")
    }
    append(release.favoritesCount.toString())
    append(" \u2605")
}