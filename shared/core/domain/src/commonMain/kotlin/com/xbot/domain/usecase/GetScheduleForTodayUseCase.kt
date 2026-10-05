package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Schedule
import com.xbot.domain.repository.ScheduleRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetScheduleForTodayUseCase(private val scheduleRepository: ScheduleRepository) :
    EitherUseCase<Unit, List<Schedule>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Schedule>> =
        scheduleRepository.getScheduleNow()
}
