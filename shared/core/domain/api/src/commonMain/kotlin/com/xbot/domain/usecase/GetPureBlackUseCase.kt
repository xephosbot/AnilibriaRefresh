package com.xbot.domain.usecase

import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow

@HiddenFromObjC
fun interface GetPureBlackUseCase {
    operator fun invoke(): Flow<Boolean>
}
