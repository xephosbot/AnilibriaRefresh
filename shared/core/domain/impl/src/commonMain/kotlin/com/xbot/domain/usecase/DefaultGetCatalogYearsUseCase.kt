package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.common.error.AppError
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogYearsUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogYearsUseCase {
    override suspend fun invoke(): Either<AppError, IntRange> =
        catalogRepository.getCatalogYears()
}
