package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.User

interface ProfileRepository {
    suspend fun getProfile(): Either<DomainError, User>
    suspend fun updateProfile(user: User): Either<DomainError, User>
}