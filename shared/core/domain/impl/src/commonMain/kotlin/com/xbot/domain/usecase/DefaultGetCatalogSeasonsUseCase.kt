package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.Season
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogSeasonsUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogSeasonsUseCase {
    override suspend fun invoke(): Either<AppError, List<Season>> =
        catalogRepository.getCatalogSeasons()
}
