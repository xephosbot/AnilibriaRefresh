package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.getOrElse
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDomain
import com.xbot.data.mapper.toDto
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.FavoriteFilters
import com.xbot.domain.repository.FavoritesRepository
import com.xbot.network.api.FavoritesApi
import com.xbot.network.models.dto.ReleaseDto
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultFavoritesRepository(
    private val api: FavoritesApi
) : FavoritesRepository {

    override suspend fun getFavoriteIds(): Either<DomainError, List<Int>> {
        return api.getFavoriteIds().mapLeft { it.toDomain() }
    }

    override fun getFavoriteReleases(
        filters: FavoriteFilters?
    ): PagingSource<Int, Release> = CommonPagingSource { page, limit ->
        val result = api.getFavoriteReleases(
            page = page,
            limit = limit,
            years = filters?.years.takeIf { it != IntRange.EMPTY }?.let { (it.start..it.endInclusive).toList() },
            types = filters?.types?.map(ReleaseType::toDto).takeIf { it?.isNotEmpty() == true },
            genres = filters?.genres?.map(Genre::id).takeIf { it?.isNotEmpty() == true },
            sorting = filters?.sortingTypes?.firstOrNull()?.toDto(),
            ageRatings = filters?.ageRatings?.map(AgeRating::toDto).takeIf { it?.isNotEmpty() == true },
        ).getOrElse { error ->
            throw error.toDomain()
        }
        CommonPagingSource.PaginatedResponse(
            items = result.data.map(ReleaseDto::toDomain),
            total = result.meta.pagination.total
        )
    }

    override suspend fun addToFavorites(releaseIds: List<Int>): Either<DomainError, Unit> {
        return api.addToFavorites(releaseIds).mapLeft { it.toDomain() }.map { Unit }
    }

    override suspend fun removeFromFavorites(releaseIds: List<Int>): Either<DomainError, Unit> {
        return api.removeFromFavorites(releaseIds).mapLeft { it.toDomain() }.map { Unit }
    }

    override suspend fun getFavoriteAgeRatings(): Either<DomainError, List<AgeRating>> {
        return api.getFavoriteAgeRatings()
            .mapLeft { it.toDomain() }
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getFavoriteGenres(): Either<DomainError, List<Genre>> {
        return api.getFavoriteGenres()
            .mapLeft { it.toDomain() }
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getFavoriteSortingTypes(): Either<DomainError, List<SortingType>> {
        return api.getFavoriteSortingTypes()
            .mapLeft { it.toDomain() }
            .map { list -> 
                list.mapNotNull {
                    when (it.name) {
                        "FRESH_AT_DESC" -> SortingType.FRESH_AT_DESC
                        "FRESH_AT_ASC" -> SortingType.FRESH_AT_ASC
                        "RATING_DESC" -> SortingType.RATING_DESC
                        "RATING_ASC" -> SortingType.RATING_ASC
                        "YEAR_DESC" -> SortingType.YEAR_DESC
                        "YEAR_ASC" -> SortingType.YEAR_ASC
                        else -> null
                    }
                }
            }
    }

    override suspend fun getFavoriteReleaseTypes(): Either<DomainError, List<ReleaseType>> {
        return api.getFavoriteReleaseTypes()
            .mapLeft { it.toDomain() }
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getFavoriteYears(): Either<DomainError, List<Int>> {
        return api.getFavoriteYears().mapLeft { it.toDomain() }
    }
}
