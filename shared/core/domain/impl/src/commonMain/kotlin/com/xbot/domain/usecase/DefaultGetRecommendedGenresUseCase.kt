package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.GenresRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetRecommendedGenresUseCase(
    private val genresRepository: GenresRepository,
) : GetRecommendedGenresUseCase {
    override suspend fun invoke(): Either<DomainError, List<Genre>> {
        return genresRepository.getRandomGenres(10)
    }
}
