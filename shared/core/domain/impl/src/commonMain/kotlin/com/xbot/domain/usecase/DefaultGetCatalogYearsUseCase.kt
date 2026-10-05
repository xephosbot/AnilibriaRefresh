package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogYearsUseCase(private val catalogRepository: CatalogRepository) :
    GetCatalogYearsUseCase {
    override suspend fun invoke(): Either<AppError, IntRange> = catalogRepository.getCatalogYears()
}
