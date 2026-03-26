package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.FavoriteSortingTypeDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.responses.PaginatedResponse

interface FavoritesApi {
    suspend fun getFavoriteIds(): Either<NetworkError, List<Int>>
    suspend fun getFavoriteReleases(
        page: Int,
        limit: Int,
        years: List<Int>? = null,
        types: List<ReleaseTypeDto>? = null,
        genres: List<Int>? = null,
        search: String? = null,
        sorting: SortingTypeDto? = null,
        ageRatings: List<AgeRatingDto>? = null
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>>
    suspend fun addToFavorites(releaseIds: List<Int>): Either<NetworkError, List<Int>>
    suspend fun removeFromFavorites(releaseIds: List<Int>): Either<NetworkError, List<Int>>
    suspend fun getFavoriteAgeRatings(): Either<NetworkError, List<AgeRatingDto>>
    suspend fun getFavoriteGenres(): Either<NetworkError, List<GenreDto>>
    suspend fun getFavoriteSortingTypes(): Either<NetworkError, List<FavoriteSortingTypeDto>>
    suspend fun getFavoriteReleaseTypes(): Either<NetworkError, List<ReleaseTypeDto>>
    suspend fun getFavoriteYears(): Either<NetworkError, List<Int>>
}
