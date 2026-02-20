package com.xbot.fixtures.repository

import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.User
import com.xbot.domain.repository.ProfileRepository
import com.xbot.fixtures.data.userMock
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Singleton

@Singleton
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
