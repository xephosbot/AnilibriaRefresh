package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.common.error.AppError
import com.xbot.data.repository.OtpRepository

class FakeOtpRepository : OtpRepository {
    override suspend fun getOtp(deviceId: String): Either<AppError, Int> = 123456.right()

    override suspend fun acceptOtp(code: Int): Either<AppError, Unit> = Unit.right()

    override suspend fun loginWithOtp(code: Int, deviceId: String): Either<AppError, String> =
        "fake_token".right()
}
