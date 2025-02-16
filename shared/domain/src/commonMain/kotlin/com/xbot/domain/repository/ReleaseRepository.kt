package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
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
    suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<Release>>>
    suspend fun getRelease(id: Int): Result<ReleaseDetail>
}
