package com.xbot.domain.usecase

import com.xbot.domain.models.ScheduleWeek
import com.xbot.domain.repository.ScheduleRepository
import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
class GetSortedScheduleWeekUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<ScheduleWeek> = combinePartial(
        { scheduleRepository.getCurrentDay() },
        { scheduleRepository.getScheduleWeek() }
    ) { currentDate, scheduleWeek ->
        ScheduleWeek.create(currentDate, scheduleWeek)
    }.flowOn(dispatcherProvider.io)
}
