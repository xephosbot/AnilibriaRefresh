package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.Error
import com.xbot.domain.models.User

interface ProfileRepository {
    suspend fun getProfile(): Either<Error, User>
    suspend fun updateProfile(user: User): Either<Error, User>
}