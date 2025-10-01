package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.DomainError

interface OtpRepository {
    suspend fun getOtp(deviceId: String): Either<DomainError, Int>
    suspend fun acceptOtp(code: Int): Either<DomainError, Unit>
    suspend fun loginWithOtp(code: Int, deviceId: String): Either<DomainError, String>
}