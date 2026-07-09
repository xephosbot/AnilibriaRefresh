package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.ProductionStatusDto
import com.xbot.network.models.enums.PublishStatusDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SeasonDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.responses.PaginatedResponse

interface CatalogApi {
    suspend fun getCatalogReleases(
        page: Int,
        limit: Int,
        genres: List<Int>? = null,
        types: List<ReleaseTypeDto>? = null,
        seasons: List<SeasonDto>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: SortingTypeDto? = null,
        ageRatings: List<AgeRatingDto>? = null,
        publishStatuses: List<PublishStatusDto>? = null,
        productionStatuses: List<ProductionStatusDto>? = null,
    ): Either<AppError, PaginatedResponse<ReleaseDto>>
    suspend fun getCatalogAgeRatings(): Either<AppError, List<AgeRatingDto>>
    suspend fun getCatalogGenres(): Either<AppError, List<GenreDto>>
    suspend fun getCatalogProductionStatuses(): Either<AppError, List<ProductionStatusDto>>
    suspend fun getCatalogPublishStatuses(): Either<AppError, List<PublishStatusDto>>
    suspend fun getCatalogSeasons(): Either<AppError, List<SeasonDto>>
    suspend fun getCatalogSortingTypes(): Either<AppError, List<SortingTypeDto>>
    suspend fun getCatalogReleaseTypes(): Either<AppError, List<ReleaseTypeDto>>
    suspend fun getCatalogYears(): Either<AppError, List<Int>>
}
