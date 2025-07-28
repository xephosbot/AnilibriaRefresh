package com.xbot.network.requests.account

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.responses.accounts.auth.LoginResponse
import com.xbot.network.models.responses.accounts.otp.OtpResponse
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun AnilibriaClient.getOtp(
    deviceId: String
): OtpResponse = request {
    post("accounts/otp/get") {
        contentType(ContentType.Application.Json)
        setBody(mapOf("device_id" to deviceId))
    }
}

suspend fun AnilibriaClient.acceptOtp(
    code: Int
): Unit = request {
    post("accounts/otp/accept") {
        contentType(ContentType.Application.Json)
        setBody(mapOf("code" to code))
    }
}

suspend fun AnilibriaClient.loginWithOtp(
    code: Int,
    deviceId: String
): LoginResponse {
    val response: LoginResponse = request {
        post("accounts/otp/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "code" to code,
                "device_id" to deviceId
            ))
        }
    }

    // Save token after successful OTP login
    setAuthToken(response.token)

    return response
}