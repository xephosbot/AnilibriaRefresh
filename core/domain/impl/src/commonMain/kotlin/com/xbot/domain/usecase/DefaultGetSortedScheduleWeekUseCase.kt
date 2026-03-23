package com.xbot.domain.usecase

import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.models.ScheduleWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetSortedScheduleWeekUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val dispatcherProvider: DispatcherProvider,
) : GetSortedScheduleWeekUseCase {
    override fun invoke(): Flow<ScheduleWeek> = combinePartial(
        { scheduleRepository.getCurrentDay() },
        { scheduleRepository.getScheduleWeek() }
    ) { currentDate, scheduleWeek ->
        ScheduleWeek.create(currentDate, scheduleWeek)
    }.flowOn(dispatcherProvider.io)
}
