package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
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
    ): Either<DomainError, PaginatedResponse<ReleaseDto>>
    suspend fun getCatalogAgeRatings(): Either<DomainError, List<AgeRatingDto>>
    suspend fun getCatalogGenres(): Either<DomainError, List<GenreDto>>
    suspend fun getCatalogProductionStatuses(): Either<DomainError, List<ProductionStatusDto>>
    suspend fun getCatalogPublishStatuses(): Either<DomainError, List<PublishStatusDto>>
    suspend fun getCatalogSeasons(): Either<DomainError, List<SeasonDto>>
    suspend fun getCatalogSortingTypes(): Either<DomainError, List<SortingTypeDto>>
    suspend fun getCatalogReleaseTypes(): Either<DomainError, List<ReleaseTypeDto>>
    suspend fun getCatalogYears(): Either<DomainError, List<Int>>
}
