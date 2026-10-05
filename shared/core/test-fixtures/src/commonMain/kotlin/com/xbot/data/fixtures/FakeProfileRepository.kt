package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.common.error.AppError
import com.xbot.domain.fixtures.userMock
import com.xbot.domain.models.User
import com.xbot.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FakeProfileRepository : ProfileRepository {
    private val user = MutableStateFlow(userMock)

    override suspend fun getProfile(): Either<AppError, User> = user.value.right()

    override suspend fun updateProfile(user: User): Either<AppError, User> {
        this.user.value = user
        return user.right()
    }
}
