package com.xbot.data.fixtures

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.FavoritesRepository
import com.xbot.domain.fixtures.GenreFixtures
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.FavoriteFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeFavoritesRepository : FavoritesRepository {
    private val favorites = MutableStateFlow(listOf(1, 2, 3))

    override suspend fun getFavoriteIds(): Either<AppError, List<Int>> {
        return favorites.value.right()
    }

    override fun getFavoriteReleases(filters: FavoriteFilters): PagingSource<Int, Release> {
        return FakePagingSource(ReleaseFixtures.all.filter { favorites.value.contains(it.id) })
    }

    override suspend fun addToFavorites(releaseIds: List<Int>): Either<AppError, Unit> {
        val current = favorites.value.toMutableList()
        current.addAll(releaseIds)
        favorites.value = current.distinct()
        return Unit.right()
    }

    override suspend fun removeFromFavorites(releaseIds: List<Int>): Either<AppError, Unit> {
        val current = favorites.value.toMutableList()
        current.removeAll(releaseIds)
        favorites.value = current
        return Unit.right()
    }

    override suspend fun getFavoriteAgeRatings(): Either<AppError, List<AgeRating>> {
        return AgeRating.entries.right()
    }

    override suspend fun getFavoriteGenres(): Either<AppError, List<Genre>> {
        return GenreFixtures.all.right()
    }

    override suspend fun getFavoriteSortingTypes(): Either<AppError, List<SortingType>> {
        return SortingType.entries.right()
    }

    override suspend fun getFavoriteReleaseTypes(): Either<AppError, List<ReleaseType>> {
        return ReleaseType.entries.right()
    }

    override suspend fun getFavoriteYears(): Either<AppError, List<Int>> {
        return (2010..2024).toList().right()
    }

    override fun observeFavorites(): Flow<List<Int>> {
        return favorites.asStateFlow()
    }
}
