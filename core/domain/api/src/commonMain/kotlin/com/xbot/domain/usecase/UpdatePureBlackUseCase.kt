package com.xbot.domain.usecase

fun interface UpdatePureBlackUseCase {
    suspend fun invoke(enabled: Boolean)
}
