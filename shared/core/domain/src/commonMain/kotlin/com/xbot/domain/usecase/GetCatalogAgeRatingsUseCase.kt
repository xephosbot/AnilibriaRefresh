package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetCatalogAgeRatingsUseCase(private val catalogRepository: CatalogRepository) :
    EitherUseCase<Unit, List<AgeRating>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<AgeRating>> =
        catalogRepository.getCatalogAgeRatings()
}
