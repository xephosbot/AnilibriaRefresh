package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import com.xbot.domain.models.AppearanceSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetAppearanceSettingsUseCase(
    private val repository: AppearanceRepository
) : GetAppearanceSettingsUseCase {
    override fun invoke(): Flow<AppearanceSettings> {
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