package com.xbot.fixtures.data

import com.xbot.domain.models.Poster
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek

val releaseMocks = listOf(
    Release(
        id = 1,
        type = ReleaseType.TV,
        year = 2024,
        name = "Frieren: Beyond Journey's End",
        englishName = "Sousou no Frieren",
        description = "The adventure is over but life goes on for an elf mage just beginning to learn what living is all about.",
        ageRating = AgeRating.R12_PLUS,
        episodesCount = 28,
        episodeDuration = 24,
        favoritesCount = 1500,
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9555/9555_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9555/9555_original.jpg")
    ),
    Release(
        id = 2,
        type = ReleaseType.TV,
        year = 2023,
        name = "Oshi no Ko",
        englishName = "My Star",
        description = "Gorou is a gynecologist and idol fan who's in shock after his favorite star, Ai, announces an impromptu hiatus.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 11,
        episodeDuration = 24,
        favoritesCount = 2000,
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9450/9450_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9450/9450_original.jpg")
    ),
    Release(
        id = 3,
        type = ReleaseType.TV,
        year = 2023,
        name = "Jujutsu Kaisen 2nd Season",
        englishName = "Sorcery Fight",
        description = "Second season of Jujutsu Kaisen.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 23,
        episodeDuration = 24,
        favoritesCount = 2500,
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9600/9600_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9600/9600_original.jpg")
    ),
    Release(
        id = 4,
        type = ReleaseType.TV,
        year = 2024,
        name = "Mushoku Tensei: Jobless Reincarnation Season 2",
        englishName = "Mushoku Tensei: Isekai Ittara Honki Dasu",
        description = "Second season of Mushoku Tensei.",
        ageRating = AgeRating.R18_PLUS,
        episodesCount = 24,
        episodeDuration = 24,
        favoritesCount = 1800,
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9200/9200_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9200/9200_original.jpg")
    ),
    Release(
        id = 5,
        type = ReleaseType.TV,
        year = 2024,
        name = "Solo Leveling",
        englishName = "Ore dake Level Up na Ken",
        description = "Ten years ago, the Gate appeared and connected the real world with the realm of magic and monsters.",
        ageRating = AgeRating.R16_PLUS,
        episodesCount = 12,
        episodeDuration = 24,
        favoritesCount = 3000,
        poster = Poster("https://www.anilibria.tv/storage/releases/posters/9700/9700_original.jpg", "https://www.anilibria.tv/storage/releases/posters/9700/9700_original.jpg")
    )
)

fun getReleaseDetailMock(id: Int): ReleaseDetail {
    val release = releaseMocks.find { it.id == id } ?: releaseMocks.first()
    return ReleaseDetail(
        release = release,
        season = Season.SPRING,
        isOngoing = true,
        publishDay = DayOfWeek.FRIDAY,
        notification = null,
        availabilityStatus = AvailabilityStatus.Available,
        genres = emptyList(),
        releaseMembers = emptyList(),
        episodes = episodeMocks,
        relatedReleases = emptyList()
    )
}
