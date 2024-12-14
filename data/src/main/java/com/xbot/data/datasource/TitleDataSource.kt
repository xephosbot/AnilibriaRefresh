package com.xbot.data.datasource

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.request.getCatalogReleases
import com.xbot.api.request.getRandomReleases
import com.xbot.api.request.getRelease
import com.xbot.api.request.getScheduleWeek
import com.xbot.data.mapper.toAgeRating
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toReleaseType
import com.xbot.data.mapper.toSeason
import com.xbot.data.models.TitlePage
import com.xbot.domain.models.EpisodeModel
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.MemberModel
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek

class TitleDataSource(
    private val client: AnilibriaClient
) {
    suspend fun getLatestTitles(page: Int, limit: Int): TitlePage {
        val titles = client.getCatalogReleases(page, limit)
        return TitlePage(
            items = titles.data.map { title ->
                TitleModel(
                    id = title.id,
                    type = title.type.value?.toReleaseType(),
                    year = title.year,
                    name = title.name.main,
                    description = title.description.orEmpty(),
                    episodesCount = title.episodesTotal,
                    favoritesCount = title.addedInUsersFavorites,
                    poster = PosterModel(
                        src = title.poster.optimized.src,
                        thumbnail = title.poster.optimized.thumbnail,
                    ),
                )
            },
            total = titles.meta.pagination.total
        )
    }

    suspend fun getRecommendedTitles(limit: Int): List<TitleModel> {
        // TODO: просто заглушка выдающая рандомные релизы
        return client.getRandomReleases(limit).map { title ->
            TitleModel(
                id = title.id,
                type = title.type.value?.toReleaseType(),
                year = title.year,
                name = title.name.main,
                description = title.description.orEmpty(),
                episodesCount = title.episodesTotal,
                favoritesCount = title.addedInUsersFavorites,
                poster = PosterModel(
                    src = title.poster.optimized.src,
                    thumbnail = title.poster.optimized.thumbnail,
                ),
            )
        }
    }

    suspend fun getScheduleTitles(): Map<DayOfWeek, List<TitleModel>> {
        return client.getScheduleWeek()
            .map { it.release }
            .groupBy(
                keySelector = { title ->
                    title.publishDay.value.toDayOfWeek()
                },
                valueTransform = { title ->
                    TitleModel(
                        id = title.id,
                        type = title.type.value?.toReleaseType(),
                        year = title.year,
                        name = title.name.main,
                        description = title.description.orEmpty(),
                        episodesCount = title.episodesTotal,
                        favoritesCount = title.addedInUsersFavorites,
                        poster = PosterModel(
                            src = title.poster.optimized.src,
                            thumbnail = title.poster.optimized.thumbnail,
                        ),
                    )
                },
            )
            .toSortedMap(compareBy { it.ordinal })
    }

    suspend fun getTitle(id: Int): TitleDetailModel {
        val title = client.getRelease(id)
        return TitleDetailModel(
            id = title.id,
            type = title.type.value?.toReleaseType(),
            year = title.year,
            name = title.name.main,
            season = title.season.value?.toSeason(),
            poster = PosterModel(
                src = title.poster.optimized.src,
                thumbnail = title.poster.optimized.thumbnail,
            ),
            isOngoing = title.isOngoing,
            ageRating = title.ageRating.value.toAgeRating(),
            publishDay = title.publishDay.value.toDayOfWeek(),
            description = title.description.orEmpty(),
            notification = title.notification.orEmpty(),
            episodesCount = title.episodesTotal,
            favoritesCount = title.addedInUsersFavorites,
            episodeDuration = title.averageDurationOfEpisode,
            genres = title.genres?.map { genre ->
                GenreModel(
                    id = genre.id,
                    name = genre.name,
                )
            } ?: listOf(),
            members = title.members?.map { member ->
                MemberModel(
                    id = member.id,
                    name = member.nickname.orEmpty(),
                    role = member.role.description.orEmpty(),
                )
            } ?: listOf(),
            episodes = title.episodes?.map { episode ->
                EpisodeModel(
                    id = episode.id,
                    name = episode.name,
                    duration = episode.duration,
                    preview = PosterModel(
                        src = episode.preview.optimized.src,
                        thumbnail = episode.preview.optimized.thumbnail,
                    ),
                    hls480 = episode.hls480,
                    hls720 = episode.hls720,
                    hls1080 = episode.hls1080,
                    ordinal = episode.ordinal,
                )
            } ?: listOf(),
        )
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
