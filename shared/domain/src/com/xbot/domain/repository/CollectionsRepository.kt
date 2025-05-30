package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.filters.CollectionFilters
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {
    suspend fun getCollectionIds(): Result<Map<Int, CollectionType>>
    fun getCollectionReleases(filters: CollectionFilters): PagingSource<Int, Release>
    suspend fun addToCollections(collections: Map<Int, CollectionType>): Result<Unit>
    suspend fun removeFromCollections(releaseIds: List<Int>): Result<Unit>
    suspend fun getCollectionAgeRatings(): Result<List<AgeRating>>
    suspend fun getCollectionGenres(): Result<List<Genre>>
    suspend fun getCollectionReleaseTypes(): Result<List<ReleaseType>>
    suspend fun getCollectionYears(): Result<List<Int>>
    fun observeCollections(): Flow<Map<Int, CollectionType>>
}