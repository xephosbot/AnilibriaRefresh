package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release

interface GenresRepository {
    suspend fun getGenres(): Either<AppError, List<Genre>>
    suspend fun getGenre(genreId: Int): Either<AppError, Genre>
    suspend fun getRandomGenres(limit: Int): Either<AppError, List<Genre>>
    fun getGenreReleases(genreId: Int): PagingSource<Int, Release>
}