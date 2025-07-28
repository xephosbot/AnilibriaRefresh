package com.xbot.domain.repository

interface OtpRepository {
    suspend fun getOtp(deviceId: String): Result<Int>
    suspend fun acceptOtp(code: Int): Result<Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Result<String>
}