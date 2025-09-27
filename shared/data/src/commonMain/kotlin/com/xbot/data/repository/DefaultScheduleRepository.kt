package com.xbot.data.repository

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Error
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.enums.Season
import com.xbot.domain.repository.ScheduleRepository
import com.xbot.network.api.ReleasesApi
import com.xbot.network.client.NetworkError
import com.xbot.network.api.ScheduleApi
import com.xbot.network.models.dto.ScheduleDto
import kotlinx.datetime.DayOfWeek

internal class DefaultScheduleRepository(
    private val scheduleApi: ScheduleApi,
    private val releasesApi: ReleasesApi,
) : ScheduleRepository {
    override suspend fun getScheduleNow(): Either<Error, List<Schedule>> = scheduleApi
        .getScheduleNow()
        .mapLeft(NetworkError::toDomain)
        .map { schedule -> schedule["today"]?.map(ScheduleDto::toDomain) ?: emptyList() }

    override suspend fun getScheduleWeek(): Either<Error, Map<DayOfWeek, List<Schedule>>> = scheduleApi
        .getScheduleWeek()
        .mapLeft(NetworkError::toDomain)
        .map { schedule ->
            schedule
                .groupBy(
                    keySelector = { schedule ->
                        schedule.release.publishDay!!.toDayOfWeek()
                    },
                    valueTransform = ScheduleDto::toDomain,
                )
                .let { map ->
                    map.entries
                        .sortedBy { it.key }
                        .associateBy({ it.key }) { it.value }
                }
        }

    override suspend fun getCurrentDay(): Either<Error, DayOfWeek> = either {
        DayOfWeek.MONDAY // Replace with actual logic to get the current day
    }

    override suspend fun getCurrentSeason(): Either<Error, Season> = releasesApi
        .getLatestReleases(10)
        .mapLeft(NetworkError::toDomain)
        .map { releases ->
            releases
                .map { it.season?.toDomain() }
                .groupingBy { it }
                .eachCount()
                .maxBy { it.value }
                .key!!
        }

    override suspend fun getCurrentYear(): Either<Error, Int> = releasesApi
        .getLatestReleases(10)
        .mapLeft(NetworkError::toDomain)
        .map { releases ->
            releases
                .map { it.year }
                .groupingBy { it }
                .eachCount()
                .maxBy { it.value }
                .key
        }
}