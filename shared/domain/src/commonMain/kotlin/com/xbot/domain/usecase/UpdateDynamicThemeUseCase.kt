package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository

class UpdateDynamicThemeUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setDynamicTheme(enabled)
    }
}