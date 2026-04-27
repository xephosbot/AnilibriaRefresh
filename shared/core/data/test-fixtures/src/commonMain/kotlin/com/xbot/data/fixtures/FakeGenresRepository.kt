package com.xbot.data.fixtures

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.GenresRepository
import com.xbot.domain.fixtures.GenreFixtures
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release

class FakeGenresRepository : GenresRepository {
    override suspend fun getGenres(): Either<DomainError, List<Genre>> {
        return GenreFixtures.all.right()
    }

    override suspend fun getGenre(genreId: Int): Either<DomainError, Genre> {
        val genre = GenreFixtures.all.find { it.id == genreId } ?: GenreFixtures.all.first()
        return genre.right()
    }

    override suspend fun getRandomGenres(limit: Int): Either<DomainError, List<Genre>> {
        return GenreFixtures.all.shuffled().take(limit).right()
    }

    override fun getGenreReleases(genreId: Int): PagingSource<Int, Release> {
        return FakePagingSource(ReleaseFixtures.all)
    }
}
