package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release

interface GenresRepository {
    suspend fun getGenres(): Either<DomainError, List<Genre>>
    suspend fun getGenre(genreId: Int): Either<DomainError, Genre>
    suspend fun getRandomGenres(limit: Int): Either<DomainError, List<Genre>>
    fun getGenreReleases(genreId: Int): PagingSource<Int, Release>
}