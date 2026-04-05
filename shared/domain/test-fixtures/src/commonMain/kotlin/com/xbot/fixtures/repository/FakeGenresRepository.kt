package com.xbot.fixtures.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.repository.GenresRepository
import com.xbot.fixtures.data.genreMocks
import com.xbot.fixtures.data.releaseMocks
import org.koin.core.annotation.Singleton

@Singleton
class FakeGenresRepository : GenresRepository {
    override suspend fun getGenres(): Either<DomainError, List<Genre>> {
        return genreMocks.right()
    }

    override suspend fun getGenre(genreId: Int): Either<DomainError, Genre> {
        val genre = genreMocks.find { it.id == genreId } ?: genreMocks.first()
        return genre.right()
    }

    override suspend fun getRandomGenres(limit: Int): Either<DomainError, List<Genre>> {
        return genreMocks.shuffled().take(limit).right()
    }

    override fun getGenreReleases(genreId: Int): PagingSource<Int, Release> {
        return FakePagingSource(releaseMocks)
    }
}
