package com.xbot.network.client

import arrow.core.Either
import co.touchlab.kermit.Logger
import com.xbot.common.error.AppError
import com.xbot.network.utils.toAppError
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.CancellationException

internal class EitherConverterFactory : Converter.Factory {

    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type != Either::class) return null
        return EitherResponseConverter(successType = typeData.typeArgs[1].typeInfo)
    }

    private inner class EitherResponseConverter(private val successType: TypeInfo) :
        Converter.SuspendResponseConverter<HttpResponse, Either<AppError, Any?>> {

        @Suppress("TooGenericExceptionCaught")
        override suspend fun convert(result: KtorfitResult): Either<AppError, Any?> =
            when (result) {
                is KtorfitResult.Failure -> result.throwable.toLeft()

                is KtorfitResult.Success -> try {
                    Either.Right(result.response.call.body(successType))
                } catch (e: Throwable) {
                    e.toLeft()
                }
            }

        private suspend fun Throwable.toLeft(): Either.Left<AppError> {
            if (this is CancellationException) throw this
            val error = toAppError()
            if (error is AppError.UnknownError) {
                Logger.withTag("Network").e(error.cause) { "Unhandled network error" }
            }
            return Either.Left(error)
        }
    }
}
