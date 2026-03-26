package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption

fun interface UpdateThemeOptionUseCase {
    suspend operator fun invoke(option: ThemeOption)
}
