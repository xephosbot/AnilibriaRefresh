package com.xbot.data.repository

import arrow.core.Either
import com.xbot.common.error.AppError

interface OtpRepository {
    suspend fun getOtp(deviceId: String): Either<AppError, Int>
    suspend fun acceptOtp(code: Int): Either<AppError, Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Either<AppError, String>
}