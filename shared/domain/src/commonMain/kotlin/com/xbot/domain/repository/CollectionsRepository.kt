package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.filters.CollectionFilters
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {
    suspend fun getCollectionIds(): Either<Error, Map<Int, CollectionType>>
    fun getCollectionReleases(filters: CollectionFilters): PagingSource<Int, Release>
    suspend fun addToCollections(collections: Map<Int, CollectionType>): Either<Error, Unit>
    suspend fun removeFromCollections(releaseIds: List<Int>): Either<Error, Unit>
    suspend fun getCollectionAgeRatings(): Either<Error, List<AgeRating>>
    suspend fun getCollectionGenres(): Either<Error, List<Genre>>
    suspend fun getCollectionReleaseTypes(): Either<Error, List<ReleaseType>>
    suspend fun getCollectionYears(): Either<Error, List<Int>>
    fun observeCollections(): Flow<Map<Int, CollectionType>>
}