package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetCatalogGenresUseCase(private val catalogRepository: CatalogRepository) :
    EitherUseCase<Unit, List<Genre>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Genre>> =
        catalogRepository.getCatalogGenres()
}
