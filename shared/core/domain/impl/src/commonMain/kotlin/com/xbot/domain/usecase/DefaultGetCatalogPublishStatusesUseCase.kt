package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.PublishStatus
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogPublishStatusesUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogPublishStatusesUseCase {
    override suspend fun invoke(): Either<DomainError, List<PublishStatus>> =
        catalogRepository.getCatalogPublishStatuses()
}
