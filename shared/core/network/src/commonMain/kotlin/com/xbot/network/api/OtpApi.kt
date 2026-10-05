package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.requests.OtpAcceptRequest
import com.xbot.network.models.requests.OtpLoginRequest
import com.xbot.network.models.requests.OtpRequest
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.OtpResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface OtpApi {
    @POST("accounts/otp/get")
    suspend fun getOtp(@Body request: OtpRequest): Either<AppError, OtpResponse>

    @POST("accounts/otp/accept")
    suspend fun acceptOtp(@Body request: OtpAcceptRequest): Either<AppError, Unit>

    @POST("accounts/otp/login")
    suspend fun loginWithOtp(@Body request: OtpLoginRequest): Either<AppError, LoginResponse>
}
