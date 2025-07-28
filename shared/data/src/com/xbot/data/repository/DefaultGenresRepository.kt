package com.xbot.data.repository

import androidx.paging.PagingSource
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.repository.GenresRepository
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.GenreApi
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.requests.anime.getGenre
import com.xbot.network.requests.anime.getGenreReleases
import com.xbot.network.requests.anime.getGenres
import com.xbot.network.requests.anime.getRandomGenres

internal class DefaultGenresRepository(
    private val client: AnilibriaClient
) : GenresRepository {
    override suspend fun getGenres(): Result<List<Genre>> = runCatching {
        client.getGenres().map(GenreApi::toDomain)
    }

    override suspend fun getGenre(genreId: Int): Result<Genre> = runCatching {
        client.getGenre(genreId).toDomain()
    }

    override suspend fun getRandomGenres(limit: Int): Result<List<Genre>> = runCatching {
        client.getRandomGenres(limit).map(GenreApi::toDomain)
    }

    override fun getGenreReleases(genreId: Int): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = client.getGenreReleases(
                    genreId = genreId,
                    page = page,
                    limit = limit,
                )
                CommonPagingSource.PaginatedResponse(
                    items = result.data.map(ReleaseApi::toDomain),
                    total = result.meta.pagination.total
                )
            }
        )
    }
}