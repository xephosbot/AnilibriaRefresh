package com.xbot.data.repository

import arrow.core.Either
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Error
import com.xbot.domain.models.Release
import com.xbot.domain.repository.ScheduleRepository
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.api.ScheduleApi
import kotlinx.datetime.DayOfWeek

internal class DefaultScheduleRepository(
    private val scheduleApi: ScheduleApi,
) : ScheduleRepository {
    override suspend fun getScheduleNow(): Either<Error, List<Release>> = scheduleApi
        .getScheduleNow()
        .mapLeft(NetworkError::toDomain)
        .map { schedule -> schedule.map { it.value.release.toDomain() } }

    override suspend fun getScheduleWeek(): Either<Error, Map<DayOfWeek, List<Release>>> = scheduleApi
        .getScheduleWeek()
        .mapLeft(NetworkError::toDomain)
        .map { schedule ->
            schedule
                .map { it.release }
                .groupBy(
                    keySelector = { title ->
                        title.publishDay!!.toDayOfWeek()
                    },
                    valueTransform = ReleaseDto::toDomain,
                )
                .let { map ->
                    map.entries
                        .sortedBy { it.key }
                        .associateBy({ it.key }) { it.value }
                }
        }
}