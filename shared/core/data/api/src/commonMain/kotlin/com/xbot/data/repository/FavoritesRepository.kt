package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.FavoriteFilters
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun getFavoriteIds(): Either<AppError, List<Int>>
    fun getFavoriteReleases(filters: FavoriteFilters): PagingSource<Int, Release>
    suspend fun addToFavorites(releaseIds: List<Int>): Either<AppError, Unit>
    suspend fun removeFromFavorites(releaseIds: List<Int>): Either<AppError, Unit>
    suspend fun getFavoriteAgeRatings(): Either<AppError, List<AgeRating>>
    suspend fun getFavoriteGenres(): Either<AppError, List<Genre>>
    suspend fun getFavoriteSortingTypes(): Either<AppError, List<SortingType>>
    suspend fun getFavoriteReleaseTypes(): Either<AppError, List<ReleaseType>>
    suspend fun getFavoriteYears(): Either<AppError, List<Int>>
    fun observeFavorites(): Flow<List<Int>>
}