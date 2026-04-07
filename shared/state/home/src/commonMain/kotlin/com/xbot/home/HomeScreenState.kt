package com.xbot.home

import com.xbot.common.AsyncResult
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.User
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class HomeScreenState(
    @Transient val currentUser: User? = null,
    @Transient val releasesFeed: ReleasesFeed = ReleasesFeed(),
    @Transient val scheduleWeek: ScheduleWeek = ScheduleWeek(),
    val currentBestType: BestType = BestType.Now,
)

@Serializable
enum class BestType { Now, AllTime }

data class ReleasesFeed(
    val recommendedReleases: AsyncResult<DomainError, List<Release>> = AsyncResult.Loading,
    val scheduleNow: AsyncResult<DomainError, List<Schedule>> = AsyncResult.Loading,
    val bestNow: AsyncResult<DomainError, List<Release>> = AsyncResult.Loading,
    val bestAllTime: AsyncResult<DomainError, List<Release>> = AsyncResult.Loading,
    val recommendedFranchises: AsyncResult<DomainError, List<Franchise>> = AsyncResult.Loading,
    val genres: AsyncResult<DomainError, List<Genre>> = AsyncResult.Loading,
)

data class ScheduleWeek(
    val days: AsyncResult<DomainError, Map<LocalDate, List<Schedule?>>> = AsyncResult.Loading
)
