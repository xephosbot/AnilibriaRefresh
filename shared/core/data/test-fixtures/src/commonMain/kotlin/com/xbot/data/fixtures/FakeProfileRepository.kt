package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.ProfileRepository
import com.xbot.domain.fixtures.userMock
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow

class FakeProfileRepository : ProfileRepository {
    private val user = MutableStateFlow(userMock)

    override suspend fun getProfile(): Either<DomainError, User> {
        return user.value.right()
    }

    override suspend fun updateProfile(user: User): Either<DomainError, User> {
        this.user.value = user
        return user.right()
    }
}
