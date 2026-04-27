package com.xbot.domain.fixtures

import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.MemberRole
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek

fun createRelease(
    id: Int = 1,
    type: ReleaseType? = ReleaseType.TV,
    year: Int = 2024,
    name: String = "Frieren: Beyond Journey's End",
    englishName: String? = "Sousou no Frieren",
    description: String? = "The adventure is over but life goes on for an elf mage just beginning to learn what living is all about.",
    ageRating: AgeRating = AgeRating.R12_PLUS,
    episodesCount: Int? = 28,
    episodeDuration: Int? = 24,
    favoritesCount: Int = 1500,
    poster: Poster? = Poster("https://www.anilibria.tv/storage/releases/posters/9555/9555_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9555/9555_original.jpg")
) = Release(
    id = id,
    type = type,
    year = year,
    name = name,
    englishName = englishName,
    description = description,
    ageRating = ageRating,
    episodesCount = episodesCount,
    episodeDuration = episodeDuration,
    favoritesCount = favoritesCount,
    poster = poster
)

fun createReleaseDetails(
    release: Release = createRelease(),
    season: Season? = Season.SPRING,
    isOngoing: Boolean = true,
    publishDay: DayOfWeek = DayOfWeek.FRIDAY,
    notification: String? = "Episode 29 will be released on October 25",
    availabilityStatus: AvailabilityStatus = AvailabilityStatus.Available,
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
    episodes: List<com.xbot.domain.models.Episode> = EpisodeFixtures.all
) = ReleaseDetails(
    release = release,
    season = season,
    isOngoing = isOngoing,
    publishDay = publishDay,
    notification = notification,
    availabilityStatus = availabilityStatus,
    genres = genres,
    releaseMembers = releaseMembers,
    episodes = episodes
)

object ReleaseFixtures {
    val frieren = createRelease(
        id = 1,
        name = "Frieren: Beyond Journey's End",
        englishName = "Sousou no Frieren",
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9555/9555_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9555/9555_original.jpg")
    )

    val oshiNoKo = createRelease(
        id = 2,
        year = 2023,
        name = "Oshi no Ko",
        englishName = "My Star",
        description = "Gorou is a gynecologist and idol fan who's in shock after his favorite star, Ai, announces an impromptu hiatus.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 11,
        favoritesCount = 2000,
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9450/9450_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9450/9450_original.jpg")
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
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9600/9600_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9600/9600_original.jpg")
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
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9200/9200_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9200/9200_original.jpg")
    )

    val soloLeveling = createRelease(
        id = 5,
        year = 2024,
        name = "Solo Leveling",
        englishName = "Ore dake Level Up na Ken",
        description = "Ten years ago, the Gate appeared and connected the real world with the realm of magic and monsters.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 12,
        favoritesCount = 3000,
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9700/9700_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9700/9700_original.jpg")
    )

    val all = listOf(frieren, oshiNoKo, jujutsuKaisen, mushokuTensei, soloLeveling)

    fun list(count: Int = 5) = all.take(count)
}
