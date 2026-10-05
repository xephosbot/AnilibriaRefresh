package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.enums.AgeRating
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogAgeRatingsUseCase(
    private val catalogRepository: CatalogRepository
) : GetCatalogAgeRatingsUseCase {
    override suspend fun invoke(): Either<AppError, List<AgeRating>> =
        catalogRepository.getCatalogAgeRatings()
}
