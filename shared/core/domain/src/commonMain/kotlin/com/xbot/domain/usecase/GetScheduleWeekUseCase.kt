package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.context.bind
import arrow.core.raise.context.either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Schedule
import com.xbot.domain.repository.ScheduleRepository
import kotlin.native.HiddenFromObjC
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetScheduleWeekUseCase(private val scheduleRepository: ScheduleRepository) :
    EitherUseCase<Unit, Map<LocalDate, List<Schedule>>> {
    override suspend fun invoke(params: Unit): Either<AppError, Map<LocalDate, List<Schedule>>> =
        either {
            val startDate = scheduleRepository.getCurrentDay().bind()
            val scheduleWeek = scheduleRepository.getScheduleWeek().bind()

            return@either (0 until DAYS_IN_WEEK).mapNotNull { offset ->
                val date = startDate.plus(offset, DateTimeUnit.DAY)
                scheduleWeek[date.dayOfWeek]?.let { date to it }
            }.toMap()
        }

    private companion object {
        const val DAYS_IN_WEEK = 7
    }
}
