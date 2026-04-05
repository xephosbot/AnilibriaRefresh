package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import com.xbot.domain.models.enums.ThemeOption
import org.koin.core.annotation.Factory

@Factory
internal class DefaultUpdateThemeOptionUseCase(
    private val repository: AppearanceRepository
) : UpdateThemeOptionUseCase {
    override suspend fun invoke(option: ThemeOption) {
        repository.setThemeOption(option)
    }
}