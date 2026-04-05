package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.OtpResponse

interface OtpApi {
    suspend fun getOtp(deviceId: String): Either<NetworkError, OtpResponse>
    suspend fun acceptOtp(code: Int): Either<NetworkError, Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Either<NetworkError, LoginResponse>
}
