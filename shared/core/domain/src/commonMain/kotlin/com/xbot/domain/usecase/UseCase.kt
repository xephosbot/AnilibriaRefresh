package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow

@HiddenFromObjC
interface UseCase<in P, out R> {
    suspend operator fun invoke(params: P): R
}

@HiddenFromObjC
interface EitherUseCase<in P, out R> {
    suspend operator fun invoke(params: P): Either<AppError, R>
}

@HiddenFromObjC
interface FlowUseCase<in P, out R> {
    operator fun invoke(params: P): Flow<R>
}

@HiddenFromObjC
suspend operator fun <R> UseCase<Unit, R>.invoke(): R = invoke(Unit)

@HiddenFromObjC
suspend operator fun <R> EitherUseCase<Unit, R>.invoke(): Either<AppError, R> = invoke(Unit)

@HiddenFromObjC
operator fun <R> FlowUseCase<Unit, R>.invoke(): Flow<R> = invoke(Unit)
