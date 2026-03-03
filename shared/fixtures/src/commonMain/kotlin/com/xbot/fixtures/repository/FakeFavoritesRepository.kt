package com.xbot.fixtures.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.FavoriteFilters
import com.xbot.domain.repository.FavoritesRepository
import com.xbot.fixtures.data.genreMocks
import com.xbot.fixtures.data.releaseMocks
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Singleton

@Singleton
class FakeFavoritesRepository : FavoritesRepository {
    private val favorites = MutableStateFlow(listOf(1, 2, 3))

    override suspend fun getFavoriteIds(): Either<DomainError, List<Int>> {
        return favorites.value.right()
    }

    override fun getFavoriteReleases(filters: FavoriteFilters?): PagingSource<Int, Release> {
        return FakePagingSource(releaseMocks.filter { favorites.value.contains(it.id) })
    }

    override suspend fun addToFavorites(releaseIds: List<Int>): Either<DomainError, Unit> {
        val current = favorites.value.toMutableList()
        current.addAll(releaseIds)
        favorites.value = current.distinct()
        return Unit.right()
    }

    override suspend fun removeFromFavorites(releaseIds: List<Int>): Either<DomainError, Unit> {
        val current = favorites.value.toMutableList()
        current.removeAll(releaseIds)
        favorites.value = current
        return Unit.right()
    }

    override suspend fun getFavoriteAgeRatings(): Either<DomainError, List<AgeRating>> {
        return AgeRating.entries.right()
    }

    override suspend fun getFavoriteGenres(): Either<DomainError, List<Genre>> {
        return genreMocks.right()
    }

    override suspend fun getFavoriteSortingTypes(): Either<DomainError, List<SortingType>> {
        return SortingType.entries.right()
    }

    override suspend fun getFavoriteReleaseTypes(): Either<DomainError, List<ReleaseType>> {
        return ReleaseType.entries.right()
    }

    override suspend fun getFavoriteYears(): Either<DomainError, List<Int>> {
        return (2010..2024).toList().right()
    }
}
