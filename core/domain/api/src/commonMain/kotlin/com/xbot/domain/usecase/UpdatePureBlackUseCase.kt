package com.xbot.domain.usecase

fun interface UpdatePureBlackUseCase {
    suspend operator fun invoke(enabled: Boolean)
}
