package com.xbot.domain.usecase

import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow

@HiddenFromObjC
fun interface GetExpressiveColorUseCase {
    operator fun invoke(): Flow<Boolean>
}
