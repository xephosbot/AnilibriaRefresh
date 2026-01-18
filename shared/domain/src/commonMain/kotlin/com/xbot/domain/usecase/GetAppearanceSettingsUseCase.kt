package com.xbot.domain.usecase

import com.xbot.domain.models.AppearanceSettings
import com.xbot.domain.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAppearanceSettingsUseCase(
    private val repository: AppearanceRepository
) {
    operator fun invoke(): Flow<AppearanceSettings> {
        return combine(
            repository.themeOption,
            repository.isDynamicTheme,
            repository.isPureBlack,
            repository.isExpressiveColor
        ) { themeOption, isDynamicTheme, isPureBlack, isExpressiveColor ->
            AppearanceSettings(
                themeOption = themeOption,
                isDynamicTheme = isDynamicTheme,
                isPureBlack = isPureBlack,
                isExpressiveColor = isExpressiveColor
            )
        }
    }
}