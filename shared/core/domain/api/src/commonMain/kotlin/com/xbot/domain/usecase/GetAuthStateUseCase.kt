package com.xbot.domain.usecase

import com.xbot.domain.models.AuthState
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow

@HiddenFromObjC
fun interface GetAuthStateUseCase {
    operator fun invoke(): Flow<AuthState>
}
