package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.OtpResponse
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultOtpApi(private val requester: ResilientHttpRequester) : OtpApi {
    override suspend fun getOtp(deviceId: String): Either<DomainError, OtpResponse> = requester.request {
        post("accounts/otp/get") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("device_id" to deviceId))
        }
    }

    override suspend fun acceptOtp(code: Int): Either<DomainError, Unit> = requester.request {
        post("accounts/otp/accept") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("code" to code))
        }
    }

    override suspend fun loginWithOtp(
        code: Int,
        deviceId: String
    ): Either<DomainError, LoginResponse> = requester.request {
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
