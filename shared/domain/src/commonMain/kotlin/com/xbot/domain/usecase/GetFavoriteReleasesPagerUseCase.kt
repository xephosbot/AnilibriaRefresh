package com.xbot.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xbot.domain.models.Release
import com.xbot.domain.models.filters.FavoriteFilters
import com.xbot.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetFavoriteReleasesPagerUseCase(
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(filters: FavoriteFilters? = null): Flow<PagingData<Release>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE,
                jumpThreshold = PAGE_SIZE * 3,
            ),
            pagingSourceFactory = {
                favoritesRepository.getFavoriteReleases(filters)
            }
        ).flow
    }

    companion object Companion {
        const val PAGE_SIZE = 20
    }
}
