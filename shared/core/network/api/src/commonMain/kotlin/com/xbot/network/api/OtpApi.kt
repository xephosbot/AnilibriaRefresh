package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.OtpResponse

interface OtpApi {
    suspend fun getOtp(deviceId: String): Either<AppError, OtpResponse>
    suspend fun acceptOtp(code: Int): Either<AppError, Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Either<AppError, LoginResponse>
}
