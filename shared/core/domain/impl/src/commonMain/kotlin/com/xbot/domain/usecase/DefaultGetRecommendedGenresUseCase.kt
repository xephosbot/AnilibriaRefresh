package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.GenresRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetRecommendedGenresUseCase(
    private val genresRepository: GenresRepository,
) : GetRecommendedGenresUseCase {
    override suspend fun invoke(): Either<AppError, List<Genre>> {
        return genresRepository.getRandomGenres(10)
    }
}
