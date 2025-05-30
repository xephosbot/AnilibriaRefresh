package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release

interface GenresRepository {
    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getGenre(genreId: Int): Result<Genre>
    suspend fun getRandomGenres(limit: Int): Result<List<Genre>>
    fun getGenreReleases(genreId: Int): PagingSource<Int, Release>
}