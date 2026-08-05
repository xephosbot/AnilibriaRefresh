package com.xbot.home

import com.xbot.common.AsyncResult
import com.xbot.common.error.AppError
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.Schedule
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * UI state for the Home screen.
 *
 * Only [currentBestType] survives process death (serialized via SavedStateHandle).
 * The three [Transient] fields are re-loaded from the network on restoration, which avoids
 * persisting potentially stale or large data structures across process death.
 */
@Serializable
data class HomeScreenState(
    @Transient val releasesFeed: ReleasesFeed = ReleasesFeed(),
    @Transient val scheduleWeek: ScheduleWeek = ScheduleWeek(),
    val currentBestType: BestType = BestType.Now,
)

@Serializable
enum class BestType { Now, AllTime }

data class ReleasesFeed(
    val recommendedReleases: AsyncResult<AppError, List<Release>> = AsyncResult.Loading,
    val scheduleNow: AsyncResult<AppError, List<Schedule>> = AsyncResult.Loading,
    val bestNow: AsyncResult<AppError, List<Release>> = AsyncResult.Loading,
    val bestAllTime: AsyncResult<AppError, List<Release>> = AsyncResult.Loading,
    val recommendedFranchises: AsyncResult<AppError, List<Franchise>> = AsyncResult.Loading,
    val genres: AsyncResult<AppError, List<Genre>> = AsyncResult.Loading,
)

data class ScheduleWeek(
    val days: AsyncResult<AppError, Map<LocalDate, List<Schedule?>>> = AsyncResult.Loading
)
