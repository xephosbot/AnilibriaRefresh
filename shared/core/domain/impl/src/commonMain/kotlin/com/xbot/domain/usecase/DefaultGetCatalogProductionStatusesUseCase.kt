package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.enums.ProductionStatus
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogProductionStatusesUseCase(
    private val catalogRepository: CatalogRepository
) : GetCatalogProductionStatusesUseCase {
    override suspend fun invoke(): Either<AppError, List<ProductionStatus>> =
        catalogRepository.getCatalogProductionStatuses()
}
