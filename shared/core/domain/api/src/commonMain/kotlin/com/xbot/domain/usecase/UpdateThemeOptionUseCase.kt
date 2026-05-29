package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface UpdateThemeOptionUseCase {
    suspend operator fun invoke(option: ThemeOption)
}
