package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogQuery
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetBestReleasesForAllTimeUseCase(private val catalogRepository: CatalogRepository) :
    EitherUseCase<Unit, List<Release>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Release>> =
        catalogRepository.getCatalogReleases(
            search = null,
            filters = CatalogQuery(sortingTypes = listOf(SortingType.RATING_DESC)),
            limit = 10
        )
}
