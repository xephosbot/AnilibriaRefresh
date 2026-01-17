package com.xbot.fixtures.repository

import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.repository.OtpRepository

class FakeOtpRepository : OtpRepository {
    override suspend fun getOtp(deviceId: String): Either<DomainError, Int> {
        return 123456.right()
    }

    override suspend fun acceptOtp(code: Int): Either<DomainError, Unit> {
        return Unit.right()
    }

    override suspend fun loginWithOtp(code: Int, deviceId: String): Either<DomainError, String> {
        return "fake_token".right()
    }
}
