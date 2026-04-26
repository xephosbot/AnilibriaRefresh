package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.fixtures.ScheduleFixtures
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

class FakeScheduleRepository : ScheduleRepository {
    override suspend fun getScheduleNow(): Either<DomainError, List<Schedule>> {
        return ScheduleFixtures.all.right()
    }

    override suspend fun getScheduleWeek(): Either<DomainError, Map<DayOfWeek, List<Schedule>>> {
        return mapOf(
            DayOfWeek.MONDAY to ScheduleFixtures.all,
            DayOfWeek.TUESDAY to ScheduleFixtures.all,
            DayOfWeek.WEDNESDAY to ScheduleFixtures.all,
            DayOfWeek.THURSDAY to ScheduleFixtures.all,
            DayOfWeek.FRIDAY to ScheduleFixtures.all,
            DayOfWeek.SATURDAY to ScheduleFixtures.all,
            DayOfWeek.SUNDAY to ScheduleFixtures.all
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
