package com.xbot.title.screen.details

import com.xbot.common.AsyncResult
import com.xbot.domain.fixtures.GenreFixtures
import com.xbot.domain.fixtures.createEpisode
import com.xbot.domain.fixtures.createRelease
import com.xbot.domain.fixtures.createReleaseDetails
import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.domain.models.Poster
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.MemberRole
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

@Suppress("MagicNumber")
internal object TitleDetailsPreviewDataV2 {

    private val poster = Poster(
        src = "https://aniliberty.top/storage/releases/posters/9893/" +
            "ItO6iCEFhNYxSmB1sSighfDiObiNyS57.webp",
        thumbnail = null
    )

    private val release = createRelease(
        id = 9893,
        alias = "kashi-ni-shita-maryoku",
        type = ReleaseType.TV,
        year = 2026,
        season = Season.WINTER,
        name = "Одолженная мной магия будет принудительно изъята",
        englishName = "Kashi ni Shita Maryoku wa Kyousei Kaishuu Shimasu",
        description = "Рэнт — обладатель уникального дара SSS-ранга, который позволяет " +
            "одалживать другим огромные запасы магии. Больше в бою он ничем помочь не " +
            "мог, за что его и изгнали из группы как «самое слабое звено».\n\nНо у " +
            "одолженной магии есть проценты. Рэнт решает собрать долги со всех, кому " +
            "когда-то помог, и вместе с должниками начинает собственный путь искателя " +
            "приключений.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 12,
        episodeDuration = 24,
        favoritesCount = 25_480,
        poster = poster
    )

    private fun episodes(count: Int, named: Boolean): List<Episode> = (1..count).map { number ->
        createEpisode(
            id = "episode-$number",
            name = if (named && number % 3 != 0) "Долг платежом красен" else null,
            englishName = null,
            ordinal = number.toFloat(),
            preview = poster,
            duration = 23.minutes + 40.seconds,
            updatedAt = LocalDateTime(2026, 1, 4, 21, 6)
        )
    } + if (count < 20) {
        listOf(
            createEpisode(
                id = "episode-special",
                name = null,
                englishName = null,
                ordinal = count + 0.5f,
                preview = poster,
                duration = 12.minutes,
                updatedAt = LocalDateTime(2026, 2, 22, 21, 6)
            )
        )
    } else {
        emptyList()
    }

    private fun progress(watchedUntil: Int): Map<String, EpisodeProgress> =
        (1..watchedUntil).associate { number ->
            val id = "episode-$number"
            id to EpisodeProgress(
                episodeId = id,
                position = if (number == watchedUntil) 11.minutes + 27.seconds else 23.minutes,
                isWatched = number < watchedUntil,
                updatedAt = LocalDateTime(2026, 10, number, 20, 0)
            )
        }

    private fun franchiseRelease(
        id: Int,
        name: String,
        type: ReleaseType,
        year: Int,
        episodes: Int?
    ) = createRelease(
        id = id,
        alias = "release-$id",
        type = type,
        year = year,
        name = name,
        episodesCount = episodes,
        isInProduction = episodes == null,
        poster = poster
    )

    private val franchiseReleases = listOf(
        franchiseRelease(1, "Одолженная мной магия", ReleaseType.TV, 2025, 12),
        franchiseRelease(3, "Одолженная мной магия: Фильм", ReleaseType.MOVIE, 2026, 1),
        franchiseRelease(4, "Одолженная мной магия 3", ReleaseType.TV, 2027, null)
    )

    private val details = createReleaseDetails(
        release = release,
        alternativeName = "I'll Collect on the Magic I Lent, Магия в долг",
        publishDay = DayOfWeek.SUNDAY,
        nextEpisodeNumber = 8,
        notification = null,
        genres = listOf(GenreFixtures.action, GenreFixtures.fantasy, GenreFixtures.comedy),
        releaseMembers = listOf(
            ReleaseMember("1", MemberRole.VOICING, "Itashi", null),
            ReleaseMember("2", MemberRole.VOICING, "Amikiri", null),
            ReleaseMember("3", MemberRole.TIMING, "Sekai", null),
            ReleaseMember("4", MemberRole.TRANSLATING, "Nika", null)
        ),
        episodes = episodes(count = 8, named = true)
    )

    val ongoing = TitleDetailsStateV2(
        details = AsyncResult.Success(details),
        franchiseReleases = AsyncResult.Success(franchiseReleases),
        episodesProgress = progress(watchedUntil = 7),
        collectionStatus = CollectionType.WATCHING,
        isFavorite = true,
        selectedTab = ReleaseTab.Episodes
    )

    private val finishedRelease = release.copy(isOngoing = false, isInProduction = false)

    private val finishedDetails = details.copy(release = finishedRelease, nextEpisodeNumber = null)

    val finished = ongoing.copy(details = AsyncResult.Success(finishedDetails))

    val geoBlocked = ongoing.copy(
        details = AsyncResult.Success(
            details.copy(availabilityStatus = AvailabilityStatus.GeoBlocked)
        )
    )

    val manyEpisodes = ongoing.copy(
        details = AsyncResult.Success(
            details.copy(
                release = release.copy(episodesCount = 300),
                episodes = episodes(count = 300, named = false)
            )
        )
    )

    val newViewer = TitleDetailsStateV2(
        details = AsyncResult.Success(finishedDetails),
        franchiseReleases = AsyncResult.Success(franchiseReleases),
        selectedTab = ReleaseTab.About
    )

    val withoutFranchise = ongoing.copy(
        franchiseReleases = AsyncResult.Success(emptyList()),
        selectedTab = ReleaseTab.About
    )

    val ratings = ongoing.copy(selectedTab = ReleaseTab.Ratings)

    val loading = TitleDetailsStateV2(initialRelease = release)
}
