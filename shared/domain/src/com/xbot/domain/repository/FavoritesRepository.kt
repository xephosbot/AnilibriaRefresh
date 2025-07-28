package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.FavoriteFilters
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun getFavoriteIds(): Result<List<Int>>
    fun getFavoriteReleases(filters: FavoriteFilters): PagingSource<Int, Release>
    suspend fun addToFavorites(releaseIds: List<Int>): Result<Unit>
    suspend fun removeFromFavorites(releaseIds: List<Int>): Result<Unit>
    suspend fun getFavoriteAgeRatings(): Result<List<AgeRating>>
    suspend fun getFavoriteGenres(): Result<List<Genre>>
    suspend fun getFavoriteSortingTypes(): Result<List<SortingType>>
    suspend fun getFavoriteReleaseTypes(): Result<List<ReleaseType>>
    suspend fun getFavoriteYears(): Result<List<Int>>
    fun observeFavorites(): Flow<List<Int>>
}