package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.fixtures.scheduleMocks
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

class FakeScheduleRepository : ScheduleRepository {
    override suspend fun getScheduleNow(): Either<DomainError, List<Schedule>> {
        return scheduleMocks.right()
    }

    override suspend fun getScheduleWeek(): Either<DomainError, Map<DayOfWeek, List<Schedule>>> {
        return mapOf(
            DayOfWeek.MONDAY to scheduleMocks,
            DayOfWeek.TUESDAY to scheduleMocks,
            DayOfWeek.WEDNESDAY to scheduleMocks,
            DayOfWeek.THURSDAY to scheduleMocks,
            DayOfWeek.FRIDAY to scheduleMocks,
            DayOfWeek.SATURDAY to scheduleMocks,
            DayOfWeek.SUNDAY to scheduleMocks
        ).right()
    }

    override suspend fun getCurrentDay(): Either<DomainError, LocalDate> {
        return LocalDate(2024, 1, 1).right()
    }

    override suspend fun getCurrentSeason(): Either<DomainError, Season> {
        return Season.SPRING.right()
    }

    override suspend fun getCurrentYear(): Either<DomainError, Int> {
        return 2024.right()
    }
}
