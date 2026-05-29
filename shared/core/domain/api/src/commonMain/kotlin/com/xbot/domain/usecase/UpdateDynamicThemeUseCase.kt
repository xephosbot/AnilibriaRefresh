package com.xbot.domain.usecase

import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface UpdateDynamicThemeUseCase {
    suspend operator fun invoke(enabled: Boolean)
}
