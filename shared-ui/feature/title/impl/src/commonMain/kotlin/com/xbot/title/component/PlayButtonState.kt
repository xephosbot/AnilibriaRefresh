package com.xbot.title.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.xbot.formatters.formatOrdinal
import com.xbot.formatters.toLocalizedUnits
import com.xbot.resources.Res
import com.xbot.resources.StringResource
import com.xbot.resources.button_watch
import com.xbot.resources.release_details_play_continue
import com.xbot.resources.release_details_play_continue_subtitle
import com.xbot.resources.release_details_play_start_subtitle
import com.xbot.title.TitleScreenState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Immutable
internal data class PlayButtonState(
    val title: StringResource,
    val subtitle: StringResource,
    val progress: Float?
)

internal val TitleScreenState.playButtonState: PlayButtonState
    @Composable get() {
        val episode = currentEpisode
        val progress = currentProgress
        return if (episode != null && progress != null) {
            PlayButtonState(
                title = StringResource.Text(Res.string.release_details_play_continue),
                subtitle = StringResource.Text(
                    Res.string.release_details_play_continue_subtitle,
                    episode.ordinal.formatOrdinal(),
                    (episode.duration?.minus(progress.position) ?: Duration.ZERO)
                        .coerceAtLeast(Duration.ZERO)
                        .toLocalizedUnits()
                ),
                progress = episode.watchedFraction(progress)
            )
        } else {
            val first = releaseDetails?.episodes?.firstOrNull()
            PlayButtonState(
                title = StringResource.Text(Res.string.button_watch),
                subtitle = StringResource.Text(
                    Res.string.release_details_play_start_subtitle,
                    (first?.ordinal ?: 1f).formatOrdinal(),
                    (first?.duration ?: release?.episodeDuration?.minutes ?: Duration.ZERO)
                        .toLocalizedUnits()
                ),
                progress = null
            )
        }
    }
