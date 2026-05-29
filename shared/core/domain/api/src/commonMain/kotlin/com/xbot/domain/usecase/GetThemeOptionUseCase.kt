package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetThemeOptionUseCase {
    operator fun invoke(): Flow<ThemeOption>
}
