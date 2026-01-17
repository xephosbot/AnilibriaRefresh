package com.xbot.fixtures.data

import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Poster

val franchiseMocks = listOf(
    Franchise(
        id = "1",
        name = "Naruto Franchise",
        englishName = "Naruto",
        rating = 8.5,
        lastYear = 2017,
        firstYear = 2002,
        totalReleases = 3,
        totalEpisodes = 720,
        totalDuration = "288 hours",
        totalDurationInSeconds = 1036800,
        poster = Poster("https://example.com/naruto.jpg", null),
        franchiseReleases = releaseMocks.take(2)
    ),
    Franchise(
        id = "2",
        name = "Attack on Titan Franchise",
        englishName = "Shingeki no Kyojin",
        rating = 9.0,
        lastYear = 2023,
        firstYear = 2013,
        totalReleases = 4,
        totalEpisodes = 88,
        totalDuration = "35 hours",
        totalDurationInSeconds = 126720,
        poster = Poster("https://example.com/aot.jpg", null),
        franchiseReleases = releaseMocks.takeLast(2)
    )
)
