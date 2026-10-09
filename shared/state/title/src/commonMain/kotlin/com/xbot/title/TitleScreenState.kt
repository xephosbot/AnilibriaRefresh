package com.xbot.title

import com.xbot.common.AsyncResult
import com.xbot.common.error.AppError
import com.xbot.common.getOrNull
import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.CollectionType

data class TitleScreenState(
    val initialRelease: Release? = null,
    val details: AsyncResult<AppError, ReleaseDetails> = AsyncResult.Loading,
    val franchiseReleases: AsyncResult<AppError, List<Release>> = AsyncResult.Loading,
    val episodesProgress: Map<String, EpisodeProgress> = emptyMap(),
    val collectionStatus: CollectionType? = null,
    val isFavorite: Boolean = false,
    val episodesSort: EpisodesSort = EpisodesSort.OldestFirst,
    val selectedTab: ReleaseTab? = null
) {
    val releaseDetails: ReleaseDetails?
        get() = details.getOrNull()

    val release: Release?
        get() = releaseDetails?.release ?: initialRelease

    val tab: ReleaseTab
        get() = selectedTab
            ?: if (currentProgress != null) ReleaseTab.Episodes else ReleaseTab.About

    val isBlocked: Boolean
        get() = releaseDetails?.availabilityStatus
            ?.let { it != AvailabilityStatus.Available } == true

    val currentProgress: EpisodeProgress?
        get() = episodesProgress.values
            .filterNot(EpisodeProgress::isWatched)
            .maxByOrNull { it.updatedAt }

    val currentEpisode: Episode?
        get() = currentProgress?.let { progress ->
            releaseDetails?.episodes?.firstOrNull { it.id == progress.episodeId }
        }

    val episodeToPlay: Episode?
        get() = currentEpisode ?: releaseDetails?.episodes?.firstOrNull()

    val sortedEpisodes: List<Episode>
        get() = releaseDetails?.episodes.orEmpty().let { episodes ->
            when (episodesSort) {
                EpisodesSort.OldestFirst -> episodes
                EpisodesSort.NewestFirst -> episodes.asReversed()
            }
        }
}

enum class ReleaseTab { Episodes, About, Ratings }

enum class EpisodesSort { OldestFirst, NewestFirst }
