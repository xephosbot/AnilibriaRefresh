package com.xbot.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetPureBlackUseCase {
    operator fun invoke(): Flow<Boolean>
}
