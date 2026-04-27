package com.xbot.domain.fixtures

import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster

fun createGenre(
    id: Int = 1,
    name: String = "Action",
    releasesCount: Int = 100,
    image: Poster? = Poster("https://example.com/action.jpg", null)
) = Genre(
    id = id,
    name = name,
    releasesCount = releasesCount,
    image = image
)

object GenreFixtures {
    val action = createGenre(id = 1, name = "Action", image = Poster("https://example.com/action.jpg", null))
    val comedy = createGenre(id = 2, name = "Comedy", releasesCount = 200, image = Poster("https://example.com/comedy.jpg", null))
    val drama = createGenre(id = 3, name = "Drama", releasesCount = 150, image = Poster("https://example.com/drama.jpg", null))
    val fantasy = createGenre(id = 4, name = "Fantasy", releasesCount = 300, image = Poster("https://example.com/fantasy.jpg", null))
    val sciFi = createGenre(id = 5, name = "Sci-Fi", releasesCount = 80, image = Poster("https://example.com/scifi.jpg", null))

    val all = listOf(action, comedy, drama, fantasy, sciFi)

    fun list(count: Int = 5) = all.take(count)
}
