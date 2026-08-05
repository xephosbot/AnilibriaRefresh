package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.OtpResponse
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultOtpApi(private val requester: HttpRequester) : OtpApi {
    override suspend fun getOtp(deviceId: String): Either<AppError, OtpResponse> = requester.request {
        post("accounts/otp/get") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("device_id" to deviceId))
        }
    }

    override suspend fun acceptOtp(code: Int): Either<AppError, Unit> = requester.request {
        post("accounts/otp/accept") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("code" to code))
        }
    }

    override suspend fun loginWithOtp(
        code: Int,
        deviceId: String
    ): Either<AppError, LoginResponse> = requester.request {
        post("accounts/otp/login") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "code" to code,
                    "device_id" to deviceId
                )
            )
        }
    }
}
