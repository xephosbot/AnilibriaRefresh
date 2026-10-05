package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetCatalogYearsUseCase(private val catalogRepository: CatalogRepository) :
    EitherUseCase<Unit, IntRange> {
    override suspend fun invoke(params: Unit): Either<AppError, IntRange> =
        catalogRepository.getCatalogYears()
}
