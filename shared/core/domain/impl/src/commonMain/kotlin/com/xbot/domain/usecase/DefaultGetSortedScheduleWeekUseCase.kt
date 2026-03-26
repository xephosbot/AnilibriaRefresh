package com.xbot.domain.usecase

import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.models.ScheduleWeek
import io.nlopez.asyncresult.getOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Factory
import kotlin.time.Clock

@Factory
internal class DefaultGetSortedScheduleWeekUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val dispatcherProvider: DispatcherProvider,
) : GetSortedScheduleWeekUseCase {
    override fun invoke(): Flow<ScheduleWeek> = combinePartial(
        { scheduleRepository.getCurrentDay() },
        { scheduleRepository.getScheduleWeek() }
    ) { currentDateResult, scheduleWeekResult ->
        val baseDate = currentDateResult.getOrNull()
            ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val scheduleMap = scheduleWeekResult.getOrNull()

        ScheduleWeek(
            days = (0..6).associate { offset ->
                val date = baseDate.plus(offset, DateTimeUnit.DAY)
                val schedules = scheduleMap?.get(date.dayOfWeek) ?: listOf(null, null)
                date to schedules
            }
        )
    }.flowOn(dispatcherProvider.io)
}
