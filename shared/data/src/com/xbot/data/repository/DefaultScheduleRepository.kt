package com.xbot.data.repository

import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Release
import com.xbot.domain.repository.ScheduleRepository
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.requests.anime.getScheduleNow
import com.xbot.network.requests.anime.getScheduleWeek
import kotlinx.datetime.DayOfWeek

internal class DefaultScheduleRepository(
    private val client: AnilibriaClient
) : ScheduleRepository {
    override suspend fun getScheduleNow(): Result<List<Release>> = runCatching {
        client.getScheduleNow()
            .map { it.value.release.toDomain() }
    }

    override suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<Release>>> = runCatching {
        client.getScheduleWeek()
            .map { it.release }
            .groupBy(
                keySelector = { title ->
                    title.publishDay!!.toDayOfWeek()
                },
                valueTransform = ReleaseApi::toDomain,
            ).let { map ->
                map.entries
                    .sortedBy { it.key }
                    .associateBy({ it.key }) { it.value }
            }
    }
}