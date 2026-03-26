package com.xbot.domain.usecase

fun interface UpdateDynamicThemeUseCase {
    suspend operator fun invoke(enabled: Boolean)
}
