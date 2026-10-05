package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow

@HiddenFromObjC
fun interface GetThemeOptionUseCase {
    operator fun invoke(): Flow<ThemeOption>
}
