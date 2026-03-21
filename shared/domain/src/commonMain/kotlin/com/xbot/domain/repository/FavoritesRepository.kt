package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.FavoriteFilters

interface FavoritesRepository {
    suspend fun getFavoriteIds(): Either<DomainError, List<Int>>
    fun getFavoriteReleases(filters: FavoriteFilters? = null): PagingSource<Int, Release>
    suspend fun addToFavorites(releaseIds: List<Int>): Either<DomainError, Unit>
    suspend fun removeFromFavorites(releaseIds: List<Int>): Either<DomainError, Unit>
    suspend fun getFavoriteAgeRatings(): Either<DomainError, List<AgeRating>>
    suspend fun getFavoriteGenres(): Either<DomainError, List<Genre>>
    suspend fun getFavoriteSortingTypes(): Either<DomainError, List<SortingType>>
    suspend fun getFavoriteReleaseTypes(): Either<DomainError, List<ReleaseType>>
    suspend fun getFavoriteYears(): Either<DomainError, List<Int>>
}