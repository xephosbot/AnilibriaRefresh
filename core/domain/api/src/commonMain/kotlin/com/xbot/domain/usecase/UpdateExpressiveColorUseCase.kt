package com.xbot.domain.usecase

fun interface UpdateExpressiveColorUseCase {
    suspend fun invoke(enabled: Boolean)
}