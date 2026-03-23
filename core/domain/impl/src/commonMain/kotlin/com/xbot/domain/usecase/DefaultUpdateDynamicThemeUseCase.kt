package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import org.koin.core.annotation.Factory

@Factory
internal class DefaultUpdateDynamicThemeUseCase(
    private val repository: AppearanceRepository
) : UpdateDynamicThemeUseCase {
    override suspend fun invoke(enabled: Boolean) {
        repository.setDynamicTheme(enabled)
    }
}
