package com.xbot.fixtures.data

import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster

val genreMocks = listOf(
    Genre(
        id = 1,
        name = "Action",
        releasesCount = 100,
        image = Poster("https://example.com/action.jpg", null)
    ),
    Genre(
        id = 2,
        name = "Comedy",
        releasesCount = 200,
        image = Poster("https://example.com/comedy.jpg", null)
    ),
    Genre(
        id = 3,
        name = "Drama",
        releasesCount = 150,
        image = Poster("https://example.com/drama.jpg", null)
    ),
    Genre(
        id = 4,
        name = "Fantasy",
        releasesCount = 300,
        image = Poster("https://example.com/fantasy.jpg", null)
    ),
    Genre(
        id = 5,
        name = "Sci-Fi",
        releasesCount = 80,
        image = Poster("https://example.com/scifi.jpg", null)
    )
)
