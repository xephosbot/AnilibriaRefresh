package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.OtpResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface OtpApi {
    suspend fun getOtp(deviceId: String): Either<NetworkError, OtpResponse>
    suspend fun acceptOtp(code: Int): Either<NetworkError, Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Either<NetworkError, LoginResponse>
}

internal class DefaultOtpApi(private val client: HttpClient) : OtpApi {
    override suspend fun getOtp(deviceId: String): Either<NetworkError, OtpResponse> = client.request {
        post("accounts/otp/get") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("device_id" to deviceId))
        }
    }

    override suspend fun acceptOtp(code: Int): Either<NetworkError, Unit> = client.request {
        post("accounts/otp/accept") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("code" to code))
        }
    }

    override suspend fun loginWithOtp(
        code: Int,
        deviceId: String
    ): Either<NetworkError, LoginResponse> = client.request {
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