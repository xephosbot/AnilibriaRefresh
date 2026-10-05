package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import com.xbot.domain.repository.GenresRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetRecommendedGenresUseCase(private val genresRepository: GenresRepository) :
    EitherUseCase<Unit, List<Genre>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Genre>> =
        genresRepository.getRandomGenres(GENRES_COUNT)

    private companion object {
        const val GENRES_COUNT = 10
    }
}
