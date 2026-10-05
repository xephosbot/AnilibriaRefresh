package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.Season
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetCatalogSeasonsUseCase(private val catalogRepository: CatalogRepository) :
    EitherUseCase<Unit, List<Season>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Season>> =
        catalogRepository.getCatalogSeasons()
}
