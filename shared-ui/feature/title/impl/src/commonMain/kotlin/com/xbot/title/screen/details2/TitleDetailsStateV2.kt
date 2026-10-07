package com.xbot.title.screen.details2

import androidx.compose.runtime.Immutable
import com.xbot.common.AsyncResult
import com.xbot.common.error.AppError
import com.xbot.common.getOrNull
import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.CollectionType
import com.xbot.formatters.formatOrdinal
import com.xbot.resources.Res
import com.xbot.resources.StringResource
import com.xbot.resources.button_watch
import com.xbot.resources.release_details_blocked_body
import com.xbot.resources.release_details_blocked_copyright_title
import com.xbot.resources.release_details_blocked_geo_title
import com.xbot.resources.release_details_play_continue
import com.xbot.resources.release_details_play_continue_subtitle
import com.xbot.resources.release_details_play_start_subtitle
import com.xbot.title.component.watchedFraction

@Immutable
internal data class TitleDetailsStateV2(
    val details: AsyncResult<AppError, ReleaseDetails> = AsyncResult.Loading,
    val franchiseReleases: AsyncResult<AppError, List<Release>> = AsyncResult.Loading,
    val episodesProgress: Map<String, EpisodeProgress> = emptyMap(),
    val collectionStatus: CollectionType? = null,
    val isFavorite: Boolean = false,
    val episodesSort: EpisodesSort = EpisodesSort.OldestFirst,
    private val initialRelease: Release? = null,
    private val selectedTab: ReleaseTab? = null
) {
    val releaseDetails: ReleaseDetails?
        get() = details.getOrNull()

    val release: Release?
        get() = releaseDetails?.release ?: initialRelease

    val tab: ReleaseTab
        get() = selectedTab
            ?: if (currentProgress != null) ReleaseTab.Episodes else ReleaseTab.About

    val isBlocked: Boolean
        get() = statusBanner is ReleaseStatusBanner.Blocked

    val statusBanner: ReleaseStatusBanner
        get() {
            val details = releaseDetails ?: return ReleaseStatusBanner.None
            val hasExternalPlayer = details.externalPlayerUrl != null
            return when (details.availabilityStatus) {
                AvailabilityStatus.GeoBlocked -> ReleaseStatusBanner.GeoBlocked(hasExternalPlayer)

                AvailabilityStatus.CopyrightBlocked ->
                    ReleaseStatusBanner.CopyrightBlocked(hasExternalPlayer)

                AvailabilityStatus.Available ->
                    details.notification
                        ?.let { ReleaseStatusBanner.Notification(StringResource.String(it)) }
                        ?: ReleaseStatusBanner.None
            }
        }

    val currentProgress: EpisodeProgress?
        get() = episodesProgress.values
            .filterNot(EpisodeProgress::isWatched)
            .maxByOrNull { it.updatedAt }

    val currentEpisode: Episode?
        get() = currentProgress?.let { progress ->
            releaseDetails?.episodes?.firstOrNull { it.id == progress.episodeId }
        }

    val playButton: PlayButtonState
        get() {
            val episode = currentEpisode
            val progress = currentProgress
            return if (episode != null && progress != null) {
                PlayButtonState(
                    title = StringResource.Text(Res.string.release_details_play_continue),
                    subtitle = StringResource.Text(
                        Res.string.release_details_play_continue_subtitle,
                        episode.ordinal.formatOrdinal(),
                        episode.duration?.minus(progress.position)
                            ?.inWholeMinutes?.toInt()?.coerceAtLeast(0) ?: 0
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
                        first?.duration?.inWholeMinutes?.toInt() ?: release?.episodeDuration ?: 0
                    ),
                    progress = null
                )
            }
        }

    val sortedEpisodes: List<Episode>
        get() = releaseDetails?.episodes.orEmpty().let { episodes ->
            when (episodesSort) {
                EpisodesSort.OldestFirst -> episodes
                EpisodesSort.NewestFirst -> episodes.asReversed()
            }
        }
}

@Immutable
internal sealed interface ReleaseStatusBanner {
    data object None : ReleaseStatusBanner

    sealed interface Blocked : ReleaseStatusBanner {
        val title: StringResource
        val hasExternalPlayer: Boolean

        val description: StringResource?
            get() = StringResource.Text(Res.string.release_details_blocked_body)
                .takeIf { hasExternalPlayer }
    }

    data class GeoBlocked(override val hasExternalPlayer: Boolean) : Blocked {
        override val title: StringResource =
            StringResource.Text(Res.string.release_details_blocked_geo_title)
    }

    data class CopyrightBlocked(override val hasExternalPlayer: Boolean) : Blocked {
        override val title: StringResource =
            StringResource.Text(Res.string.release_details_blocked_copyright_title)
    }

    data class Notification(val text: StringResource) : ReleaseStatusBanner
}

@Immutable
internal data class PlayButtonState(
    val title: StringResource,
    val subtitle: StringResource,
    val progress: Float?
)

internal enum class ReleaseTab { Episodes, About, Ratings }

internal enum class EpisodesSort { OldestFirst, NewestFirst }

internal sealed interface TitleDetailsActionV2 {
    data object OnPlayClick : TitleDetailsActionV2
    data object OnFavoriteToggle : TitleDetailsActionV2
    data class OnCollectionStatusSelect(val status: CollectionType?) : TitleDetailsActionV2
    data class OnTabSelect(val tab: ReleaseTab) : TitleDetailsActionV2
    data class OnEpisodesSortChange(val sort: EpisodesSort) : TitleDetailsActionV2
    data class OnEpisodeClick(val episode: Episode) : TitleDetailsActionV2
    data class OnFranchiseReleaseClick(val release: Release) : TitleDetailsActionV2
    data class OnGenreClick(val genre: Genre) : TitleDetailsActionV2
    data class OnMemberClick(val member: ReleaseMember) : TitleDetailsActionV2
    data object OnFranchiseAllClick : TitleDetailsActionV2
    data object OnExternalPlayerClick : TitleDetailsActionV2
    data class OnUrlClick(val url: String) : TitleDetailsActionV2
    data object OnShareClick : TitleDetailsActionV2
    data object OnCopyLinkClick : TitleDetailsActionV2
    data class OnCopyTextClick(val text: String) : TitleDetailsActionV2
}
