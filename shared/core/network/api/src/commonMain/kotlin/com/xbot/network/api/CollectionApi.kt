package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.CollectionTypeDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.responses.PaginatedResponse

interface CollectionApi {
    suspend fun getCollectionIds(): Either<AppError, Map<Int, CollectionTypeDto>>
    suspend fun getCollectionReleases(
        page: Int,
        limit: Int,
        collectionType: CollectionTypeDto,
        genres: List<Int>? = null,
        types: List<ReleaseTypeDto>? = null,
        years: List<Int>? = null,
        search: String? = null,
        ageRatings: List<AgeRatingDto>? = null
    ): Either<AppError, PaginatedResponse<ReleaseDto>>
    suspend fun addToCollections(
        collections: Map<Int, CollectionTypeDto> // releaseId to collectionType
    ): Either<AppError, Map<Int, CollectionTypeDto>>
    suspend fun removeFromCollections(
        releaseIds: List<Int>
    ): Either<AppError, Map<Int, CollectionTypeDto>>
    suspend fun getCollectionAgeRatings(): Either<AppError, List<AgeRatingDto>>
    suspend fun getCollectionGenres(): Either<AppError, List<GenreDto>>
    suspend fun getCollectionReleaseTypes(): Either<AppError, List<ReleaseTypeDto>>
    suspend fun getCollectionYears(): Either<AppError, List<Int>>
}
