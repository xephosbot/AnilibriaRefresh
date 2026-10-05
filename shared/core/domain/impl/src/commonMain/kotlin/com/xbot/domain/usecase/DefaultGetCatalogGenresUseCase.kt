package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.Genre
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogGenresUseCase(private val catalogRepository: CatalogRepository) :
    GetCatalogGenresUseCase {
    override suspend fun invoke(): Either<AppError, List<Genre>> =
        catalogRepository.getCatalogGenres()
}
