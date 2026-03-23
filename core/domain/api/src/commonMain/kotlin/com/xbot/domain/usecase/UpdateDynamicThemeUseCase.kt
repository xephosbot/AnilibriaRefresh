package com.xbot.domain.usecase

fun interface UpdateDynamicThemeUseCase {
    suspend fun invoke(enabled: Boolean)
}
