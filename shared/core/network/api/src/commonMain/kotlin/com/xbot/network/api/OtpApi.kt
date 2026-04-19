package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.OtpResponse

interface OtpApi {
    suspend fun getOtp(deviceId: String): Either<DomainError, OtpResponse>
    suspend fun acceptOtp(code: Int): Either<DomainError, Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Either<DomainError, LoginResponse>
}
