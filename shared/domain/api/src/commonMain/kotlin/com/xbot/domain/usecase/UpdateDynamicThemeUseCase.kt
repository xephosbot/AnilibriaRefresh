package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateDynamicThemeUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setDynamicTheme(enabled)
    }
}
