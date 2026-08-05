package com.xbot.data.fixtures

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.CollectionsRepository
import com.xbot.domain.fixtures.GenreFixtures
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.filters.CollectionFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCollectionsRepository : CollectionsRepository {
    private val collections = MutableStateFlow(
        mapOf(
            1 to CollectionType.WATCHING,
            2 to CollectionType.PLANNED
        )
    )

    override suspend fun getCollectionIds(): Either<AppError, Map<Int, CollectionType>> {
        return collections.value.right()
    }

    override fun getCollectionReleases(filters: CollectionFilters): PagingSource<Int, Release> {
        return FakePagingSource(ReleaseFixtures.all)
    }

    override suspend fun addToCollections(collections: Map<Int, CollectionType>): Either<AppError, Unit> {
        val current = this.collections.value.toMutableMap()
        current.putAll(collections)
        this.collections.value = current
        return Unit.right()
    }

    override suspend fun removeFromCollections(releaseIds: List<Int>): Either<AppError, Unit> {
        val current = this.collections.value.toMutableMap()
        releaseIds.forEach { current.remove(it) }
        this.collections.value = current
        return Unit.right()
    }

    override suspend fun getCollectionAgeRatings(): Either<AppError, List<AgeRating>> {
        return AgeRating.entries.right()
    }

    override suspend fun getCollectionGenres(): Either<AppError, List<Genre>> {
        return GenreFixtures.all.right()
    }

    override suspend fun getCollectionReleaseTypes(): Either<AppError, List<ReleaseType>> {
        return ReleaseType.entries.right()
    }

    override suspend fun getCollectionYears(): Either<AppError, List<Int>> {
        return (2000..2024).toList().right()
    }

    override fun observeCollections(): Flow<Map<Int, CollectionType>> {
        return collections.asStateFlow()
    }
}
