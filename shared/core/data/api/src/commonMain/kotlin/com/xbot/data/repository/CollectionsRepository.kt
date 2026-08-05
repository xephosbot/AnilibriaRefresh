package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.filters.CollectionFilters
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {
    suspend fun getCollectionIds(): Either<AppError, Map<Int, CollectionType>>
    fun getCollectionReleases(filters: CollectionFilters): PagingSource<Int, Release>
    suspend fun addToCollections(collections: Map<Int, CollectionType>): Either<AppError, Unit>
    suspend fun removeFromCollections(releaseIds: List<Int>): Either<AppError, Unit>
    suspend fun getCollectionAgeRatings(): Either<AppError, List<AgeRating>>
    suspend fun getCollectionGenres(): Either<AppError, List<Genre>>
    suspend fun getCollectionReleaseTypes(): Either<AppError, List<ReleaseType>>
    suspend fun getCollectionYears(): Either<AppError, List<Int>>
    fun observeCollections(): Flow<Map<Int, CollectionType>>
}