package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption
import kotlinx.coroutines.flow.Flow

fun interface GetThemeOptionUseCase {
    operator fun invoke(): Flow<ThemeOption>
}
