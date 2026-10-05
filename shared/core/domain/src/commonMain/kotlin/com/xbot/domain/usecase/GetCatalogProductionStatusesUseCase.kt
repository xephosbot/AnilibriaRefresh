package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetCatalogProductionStatusesUseCase(private val catalogRepository: CatalogRepository) :
    EitherUseCase<Unit, List<ProductionStatus>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<ProductionStatus>> =
        catalogRepository.getCatalogProductionStatuses()
}
