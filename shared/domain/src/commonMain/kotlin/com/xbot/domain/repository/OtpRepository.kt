package com.xbot.domain.repository

import arrow.core.Either

interface OtpRepository {
    suspend fun getOtp(deviceId: String): Either<Error, Int>
    suspend fun acceptOtp(code: Int): Either<Error, Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Either<Error, String>
}