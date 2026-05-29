package com.xbot.domain.usecase

import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface UpdateExpressiveColorUseCase {
    suspend operator fun invoke(enabled: Boolean)
}
