package com.xbot.shared.domain.repository

import androidx.paging.PagingSource
import com.xbot.shared.domain.models.Genre
import com.xbot.shared.domain.models.Release
import com.xbot.shared.domain.models.ReleaseDetail
import com.xbot.shared.domain.models.enums.AgeRating
import com.xbot.shared.domain.models.enums.ProductionStatus
import com.xbot.shared.domain.models.enums.PublishStatus
import com.xbot.shared.domain.models.enums.ReleaseType
import com.xbot.shared.domain.models.enums.Season
import com.xbot.shared.domain.models.enums.SortingType
import kotlinx.datetime.DayOfWeek

interface ReleaseRepository {
    fun getReleasePagingSource(
        search: String?,
        genres: List<Genre>?,
        types: List<ReleaseType>?,
        seasons: List<Season>?,
        yearsRange: ClosedRange<Int>?,
        sorting: SortingType?,
        ageRatings: List<AgeRating>?,
        publishStatuses: List<PublishStatus>?,
        productionStatuses: List<ProductionStatus>?,
    ): PagingSource<Int, Release>
    suspend fun getRecommendedReleases(): Result<List<Release>>
    suspend fun getRecommendedGenres(): Result<List<Genre>>
    suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<Release>>>
    suspend fun getRelease(aliasOrId: String): Result<ReleaseDetail>
}
