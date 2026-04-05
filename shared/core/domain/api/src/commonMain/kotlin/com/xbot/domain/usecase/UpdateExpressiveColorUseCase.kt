package com.xbot.domain.usecase

fun interface UpdateExpressiveColorUseCase {
    suspend operator fun invoke(enabled: Boolean)
}