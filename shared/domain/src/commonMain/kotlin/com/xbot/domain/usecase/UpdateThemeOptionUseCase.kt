package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.repository.AppearanceRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateThemeOptionUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(option: ThemeOption) {
        repository.setThemeOption(option)
    }
}