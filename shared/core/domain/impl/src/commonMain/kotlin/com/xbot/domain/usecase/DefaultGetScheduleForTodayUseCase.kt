package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.ScheduleRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.Schedule
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetScheduleForTodayUseCase(
    private val scheduleRepository: ScheduleRepository,
) : GetScheduleForTodayUseCase {
    override suspend fun invoke(): Either<AppError, List<Schedule>> {
        return scheduleRepository.getScheduleNow()
    }
}
