package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.getOrElse
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.repository.GenresRepository
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.api.GenresApi

internal class DefaultGenresRepository(
    private val genresApi: GenresApi,
) : GenresRepository {
    override suspend fun getGenres(): Either<DomainError, List<Genre>> = genresApi
        .getGenres()
        .mapLeft(NetworkError::toDomain)
        .map { it.map(GenreDto::toDomain) }

    override suspend fun getGenre(genreId: Int): Either<DomainError, Genre> = genresApi
        .getGenre(genreId)
        .mapLeft(NetworkError::toDomain)
        .map(GenreDto::toDomain)

    override suspend fun getRandomGenres(limit: Int): Either<DomainError, List<Genre>> = genresApi
        .getRandomGenres(limit)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(GenreDto::toDomain) }

    override fun getGenreReleases(genreId: Int): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = genresApi.getGenreReleases(
                    genreId = genreId,
                    page = page,
                    limit = limit,
                ).getOrElse { error ->
                    throw IllegalStateException()
                }
                CommonPagingSource.PaginatedResponse(
                    items = result.data.map(ReleaseDto::toDomain),
                    total = result.meta.pagination.total
                )
            }
        )
    }
}