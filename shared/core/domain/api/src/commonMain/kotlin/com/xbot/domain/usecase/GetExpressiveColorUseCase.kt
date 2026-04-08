package com.xbot.domain.usecase

import kotlinx.coroutines.flow.Flow

fun interface GetExpressiveColorUseCase {
    operator fun invoke(): Flow<Boolean>
}
