package com.xbot.data.repository

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.User

interface ProfileRepository {
    suspend fun getProfile(): Either<AppError, User>
    suspend fun updateProfile(user: User): Either<AppError, User>
}