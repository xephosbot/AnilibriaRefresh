package com.xbot.domain.usecase

import com.xbot.domain.models.AuthState
import kotlinx.coroutines.flow.Flow

fun interface GetAuthStateUseCase {
    operator fun invoke(): Flow<AuthState>
}
