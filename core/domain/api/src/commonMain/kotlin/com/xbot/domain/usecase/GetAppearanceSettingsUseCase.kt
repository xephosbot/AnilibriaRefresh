package com.xbot.domain.usecase

import com.xbot.domain.models.AppearanceSettings
import kotlinx.coroutines.flow.Flow

fun interface GetAppearanceSettingsUseCase {
    operator fun invoke(): Flow<AppearanceSettings>
}