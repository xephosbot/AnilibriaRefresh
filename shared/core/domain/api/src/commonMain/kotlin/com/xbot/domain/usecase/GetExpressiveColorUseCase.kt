package com.xbot.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetExpressiveColorUseCase {
    operator fun invoke(): Flow<Boolean>
}
