package com.xbot.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.coroutines.flow.Flow

class GetReleasesPager(
    private val releaseRepository: ReleaseRepository
) {
    operator fun invoke(
        search: String? = null,
        genres: List<Genre>? = null,
        types: List<ReleaseType>? = null,
        seasons: List<Season>? = null,
        yearsRange: ClosedRange<Int>? = null,
        sorting: SortingType? = null,
        ageRatings: List<AgeRating>? = null,
        publishStatuses: List<PublishStatus>? = null,
        productionStatuses: List<ProductionStatus>? = null,
    ): Flow<PagingData<Release>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE,
                jumpThreshold = PAGE_SIZE * 3,
            ),
            pagingSourceFactory = {
                releaseRepository.getReleasePagingSource(
                    search = search,
                    genres = genres,
                    types = types,
                    seasons = seasons,
                    yearsRange = yearsRange,
                    sorting = sorting,
                    ageRatings = ageRatings,
                    publishStatuses = publishStatuses,
                    productionStatuses = productionStatuses,
                )
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}