package com.xbot.domain.usecase

import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface UpdatePureBlackUseCase {
    suspend operator fun invoke(enabled: Boolean)
}
