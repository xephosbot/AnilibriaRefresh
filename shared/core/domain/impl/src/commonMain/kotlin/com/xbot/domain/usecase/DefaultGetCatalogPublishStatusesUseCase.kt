package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.enums.PublishStatus
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogPublishStatusesUseCase(
    private val catalogRepository: CatalogRepository
) : GetCatalogPublishStatusesUseCase {
    override suspend fun invoke(): Either<AppError, List<PublishStatus>> =
        catalogRepository.getCatalogPublishStatuses()
}
