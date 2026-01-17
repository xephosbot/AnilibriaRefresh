package com.xbot.fixtures.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.filters.CollectionFilters
import com.xbot.domain.repository.CollectionsRepository
import com.xbot.fixtures.data.genreMocks
import com.xbot.fixtures.data.releaseMocks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeCollectionsRepository : CollectionsRepository {
    private val collections = MutableStateFlow<Map<Int, CollectionType>>(
        mapOf(
            1 to CollectionType.WATCHING,
            2 to CollectionType.PLANNED
        )
    )

    override suspend fun getCollectionIds(): Either<DomainError, Map<Int, CollectionType>> {
        return collections.value.right()
    }

    override fun getCollectionReleases(filters: CollectionFilters): PagingSource<Int, Release> {
        return FakePagingSource(releaseMocks)
    }

    override suspend fun addToCollections(collections: Map<Int, CollectionType>): Either<DomainError, Unit> {
        val current = this.collections.value.toMutableMap()
        current.putAll(collections)
        this.collections.value = current
        return Unit.right()
    }

    override suspend fun removeFromCollections(releaseIds: List<Int>): Either<DomainError, Unit> {
        val current = this.collections.value.toMutableMap()
        releaseIds.forEach { current.remove(it) }
        this.collections.value = current
        return Unit.right()
    }

    override suspend fun getCollectionAgeRatings(): Either<DomainError, List<AgeRating>> {
        return AgeRating.entries.right()
    }

    override suspend fun getCollectionGenres(): Either<DomainError, List<Genre>> {
        return genreMocks.right()
    }

    override suspend fun getCollectionReleaseTypes(): Either<DomainError, List<ReleaseType>> {
        return ReleaseType.entries.right()
    }

    override suspend fun getCollectionYears(): Either<DomainError, List<Int>> {
        return (2000..2024).toList().right()
    }

    override fun observeCollections(): Flow<Map<Int, CollectionType>> {
        return collections.asStateFlow()
    }
}
