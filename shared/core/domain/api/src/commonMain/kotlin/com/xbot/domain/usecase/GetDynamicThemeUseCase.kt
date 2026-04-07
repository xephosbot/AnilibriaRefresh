package com.xbot.domain.usecase

import kotlinx.coroutines.flow.Flow

fun interface GetDynamicThemeUseCase {
    operator fun invoke(): Flow<Boolean>
}
