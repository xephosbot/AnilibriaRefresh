package com.xbot.domain.fixtures

import com.xbot.domain.models.ExternalRating
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.ReleaseRating
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.MemberRole
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

private const val POSTER_URL =
    "https://anilibria.top/storage/releases/posters/9893/ItO6iCEFhNYxSmB1sSighfDiObiNyS57.webp"
private const val LEGACY_POSTER_URL =
    "https://www.anilibria.tv/storage/releases/posters/9893/ItO6iCEFhNYxSmB1sSighfDiObiNyS57.webp"

private val defaultPoster = Poster(POSTER_URL, POSTER_URL)

fun createRelease(
    id: Int = 1,
    alias: String = "sousou-no-frieren",
    type: ReleaseType? = ReleaseType.TV,
    year: Int = 2024,
    season: Season? = Season.SPRING,
    name: String = "Frieren: Beyond Journey's End",
    englishName: String? = "Sousou no Frieren",
    description: String? = "The adventure is over but life goes on for an elf mage " +
        "just beginning to learn what living is all about.",
    ageRating: AgeRating = AgeRating.R12_PLUS,
    episodesCount: Int? = 28,
    episodeDuration: Int? = 24,
    favoritesCount: Int = 1500,
    isOngoing: Boolean = true,
    isInProduction: Boolean = false,
    poster: Poster? = defaultPoster
) = Release(
    id = id,
    alias = alias,
    type = type,
    year = year,
    season = season,
    name = name,
    englishName = englishName,
    description = description,
    ageRating = ageRating,
    episodesCount = episodesCount,
    episodeDuration = episodeDuration,
    favoritesCount = favoritesCount,
    isOngoing = isOngoing,
    isInProduction = isInProduction,
    poster = poster
)

fun createReleaseDetails(
    release: Release = createRelease(),
    alternativeName: String? = "Frieren, Провожающая в последний путь Фрирен",
    publishDay: DayOfWeek = DayOfWeek.FRIDAY,
    nextEpisodeNumber: Int? = 29,
    notification: String? = "Episode 29 will be released on October 25",
    availabilityStatus: AvailabilityStatus = AvailabilityStatus.Available,
    externalPlayerUrl: String? = "https://kodik.info/serial/1/hash/720p",
    freshAt: LocalDateTime? = LocalDateTime(2026, 10, 4, 18, 0),
    genres: List<Genre> = listOf(
        Genre(1, "Fantasy", 100, null),
        Genre(2, "Adventure", 80, null),
        Genre(3, "Drama", 50, null)
    ),
    releaseMembers: List<ReleaseMember> = listOf(
        ReleaseMember("1", MemberRole.VOICING, "Lupin", null),
        ReleaseMember("2", MemberRole.VOICING, "Silv", null),
        ReleaseMember("3", MemberRole.TIMING, "Mimal", null),
        ReleaseMember("4", MemberRole.TRANSLATING, "Arta", null)
    ),
    episodes: List<com.xbot.domain.models.Episode> = EpisodeFixtures.all,
    rating: ReleaseRating? = ReleaseRating(
        average = 8.25,
        votes = 1204,
        votesByScore = mapOf(
            10 to 420, 9 to 310, 8 to 220, 7 to 120, 6 to 60, 5 to 30, 4 to 18,
            3 to 12, 2 to 6, 1 to 8
        )
    ),
    shikimoriRating: ExternalRating? =
        ExternalRating(7.23, 129_000, "https://shikimori.one/animes/52991"),
    myAnimeListRating: ExternalRating? =
        ExternalRating(7.21, 1_900_000, "https://myanimelist.net/anime/52991"),
    collectionCounts: Map<CollectionType, Int> = mapOf(
        CollectionType.WATCHING to 8_120,
        CollectionType.PLANNED to 5_430,
        CollectionType.WATCHED to 3_210,
        CollectionType.POSTPONED to 640,
        CollectionType.ABANDONED to 52
    )
) = ReleaseDetails(
    release = release,
    alternativeName = alternativeName,
    publishDay = publishDay,
    nextEpisodeNumber = nextEpisodeNumber,
    notification = notification,
    availabilityStatus = availabilityStatus,
    externalPlayerUrl = externalPlayerUrl,
    freshAt = freshAt,
    genres = genres,
    releaseMembers = releaseMembers,
    episodes = episodes,
    rating = rating,
    shikimoriRating = shikimoriRating,
    myAnimeListRating = myAnimeListRating,
    collectionCounts = collectionCounts
)

object ReleaseFixtures {
    val frieren = createRelease(
        id = 1,
        name = "Frieren: Beyond Journey's End",
        englishName = "Sousou no Frieren",
        poster = defaultPoster
    )

    val oshiNoKo = createRelease(
        id = 2,
        year = 2023,
        name = "Oshi no Ko",
        englishName = "My Star",
        description = "Gorou is a gynecologist and idol fan who's in shock " +
            "after his favorite star, Ai, announces an impromptu hiatus.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 11,
        favoritesCount = 2000,
        poster = defaultPoster
    )

    val jujutsuKaisen = createRelease(
        id = 3,
        year = 2023,
        name = "Jujutsu Kaisen 2nd Season",
        englishName = "Sorcery Fight",
        description = "Second season of Jujutsu Kaisen.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 23,
        favoritesCount = 2500,
        poster = Poster(LEGACY_POSTER_URL, POSTER_URL)
    )

    val mushokuTensei = createRelease(
        id = 4,
        year = 2024,
        name = "Mushoku Tensei: Jobless Reincarnation Season 2",
        englishName = "Mushoku Tensei: Isekai Ittara Honki Dasu",
        description = "Second season of Mushoku Tensei.",
        ageRating = AgeRating.R18_PLUS,
        episodesCount = 24,
        favoritesCount = 1800,
        poster = defaultPoster
    )

    val soloLeveling = createRelease(
        id = 5,
        year = 2024,
        name = "Solo Leveling",
        englishName = "Ore dake Level Up na Ken",
        description = "Ten years ago, the Gate appeared and connected the real world " +
            "with the realm of magic and monsters.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 12,
        favoritesCount = 3000,
        poster = defaultPoster
    )

    val all = listOf(frieren, oshiNoKo, jujutsuKaisen, mushokuTensei, soloLeveling)

    fun list(count: Int = 5) = all.take(count)
}
