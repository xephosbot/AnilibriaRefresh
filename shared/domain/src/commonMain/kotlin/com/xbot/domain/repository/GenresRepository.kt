package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.Error
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release

interface GenresRepository {
    suspend fun getGenres(): Either<Error, List<Genre>>
    suspend fun getGenre(genreId: Int): Either<Error, Genre>
    suspend fun getRandomGenres(limit: Int): Either<Error, List<Genre>>
    fun getGenreReleases(genreId: Int): PagingSource<Int, Release>
}