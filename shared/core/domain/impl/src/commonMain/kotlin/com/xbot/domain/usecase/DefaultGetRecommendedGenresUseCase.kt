package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.GenresRepository
import com.xbot.domain.models.Genre
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetRecommendedGenresUseCase(private val genresRepository: GenresRepository) :
    GetRecommendedGenresUseCase {
    override suspend fun invoke(): Either<AppError, List<Genre>> =
        genresRepository.getRandomGenres(10)
}
