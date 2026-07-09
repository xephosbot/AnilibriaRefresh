package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.OtpRepository
import com.xbot.common.error.AppError

class FakeOtpRepository : OtpRepository {
    override suspend fun getOtp(deviceId: String): Either<AppError, Int> {
        return 123456.right()
    }

    override suspend fun acceptOtp(code: Int): Either<AppError, Unit> {
        return Unit.right()
    }

    override suspend fun loginWithOtp(code: Int, deviceId: String): Either<AppError, String> {
        return "fake_token".right()
    }
}
