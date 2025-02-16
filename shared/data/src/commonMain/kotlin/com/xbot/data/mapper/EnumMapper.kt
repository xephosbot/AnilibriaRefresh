package com.xbot.data.mapper

import com.xbot.api.models.shared.enums.AgeRatingApi
import com.xbot.api.models.shared.enums.ProductionStatusApi
import com.xbot.api.models.shared.enums.PublishDayApi
import com.xbot.api.models.shared.enums.PublishStatusApi
import com.xbot.api.models.shared.enums.ReleaseTypeApi
import com.xbot.api.models.shared.enums.SeasonApi
import com.xbot.api.models.shared.enums.SortingTypeApi
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import kotlinx.datetime.DayOfWeek

internal fun AgeRatingApi.toDomain(): AgeRating = when (this) {
    AgeRatingApi.R0_PLUS -> AgeRating.R0_PLUS
    AgeRatingApi.R6_PLUS -> AgeRating.R6_PLUS
    AgeRatingApi.R12_PLUS -> AgeRating.R12_PLUS
    AgeRatingApi.R16_PLUS -> AgeRating.R16_PLUS
    AgeRatingApi.R18_PLUS -> AgeRating.R18_PLUS
}

internal fun AgeRating.toApi(): AgeRatingApi = when (this) {
    AgeRating.R0_PLUS -> AgeRatingApi.R0_PLUS
    AgeRating.R6_PLUS -> AgeRatingApi.R6_PLUS
    AgeRating.R12_PLUS -> AgeRatingApi.R12_PLUS
    AgeRating.R16_PLUS -> AgeRatingApi.R16_PLUS
    AgeRating.R18_PLUS -> AgeRatingApi.R18_PLUS
}

internal fun ProductionStatusApi.toDomain(): ProductionStatus = when (this) {
    ProductionStatusApi.IS_IN_PRODUCTION -> ProductionStatus.IS_IN_PRODUCTION
    ProductionStatusApi.IS_NOT_IN_PRODUCTION -> ProductionStatus.IS_NOT_IN_PRODUCTION
}

internal fun ProductionStatus.toApi(): ProductionStatusApi = when (this) {
    ProductionStatus.IS_IN_PRODUCTION -> ProductionStatusApi.IS_IN_PRODUCTION
    ProductionStatus.IS_NOT_IN_PRODUCTION -> ProductionStatusApi.IS_NOT_IN_PRODUCTION
}

internal fun PublishStatusApi.toDomain(): PublishStatus = when (this) {
    PublishStatusApi.IS_ONGOING -> PublishStatus.IS_ONGOING
    PublishStatusApi.IS_NOT_ONGOING -> PublishStatus.IS_NOT_ONGOING
}

internal fun PublishStatus.toApi(): PublishStatusApi = when (this) {
    PublishStatus.IS_ONGOING -> PublishStatusApi.IS_ONGOING
    PublishStatus.IS_NOT_ONGOING -> PublishStatusApi.IS_NOT_ONGOING
}

internal fun ReleaseTypeApi.toDomain(): ReleaseType = when (this) {
    ReleaseTypeApi.OAD -> ReleaseType.OAD
    ReleaseTypeApi.ONA -> ReleaseType.ONA
    ReleaseTypeApi.OVA -> ReleaseType.OVA
    ReleaseTypeApi.WEB -> ReleaseType.WEB
    ReleaseTypeApi.DORAMA -> ReleaseType.DORAMA
    ReleaseTypeApi.SPECIAL -> ReleaseType.SPECIAL
    ReleaseTypeApi.MOVIE -> ReleaseType.MOVIE
    ReleaseTypeApi.TV -> ReleaseType.TV
}

internal fun ReleaseType.toApi(): ReleaseTypeApi = when (this) {
    ReleaseType.OAD -> ReleaseTypeApi.OAD
    ReleaseType.ONA -> ReleaseTypeApi.ONA
    ReleaseType.OVA -> ReleaseTypeApi.OVA
    ReleaseType.WEB -> ReleaseTypeApi.WEB
    ReleaseType.DORAMA -> ReleaseTypeApi.DORAMA
    ReleaseType.SPECIAL -> ReleaseTypeApi.SPECIAL
    ReleaseType.MOVIE -> ReleaseTypeApi.MOVIE
    ReleaseType.TV -> ReleaseTypeApi.TV
}

internal fun PublishDayApi.toDayOfWeek(): DayOfWeek = when (this) {
    PublishDayApi.MONDAY -> DayOfWeek.MONDAY
    PublishDayApi.TUESDAY -> DayOfWeek.TUESDAY
    PublishDayApi.WEDNESDAY -> DayOfWeek.WEDNESDAY
    PublishDayApi.THURSDAY -> DayOfWeek.THURSDAY
    PublishDayApi.FRIDAY -> DayOfWeek.FRIDAY
    PublishDayApi.SATURDAY -> DayOfWeek.SATURDAY
    PublishDayApi.SUNDAY -> DayOfWeek.SUNDAY
}

internal fun SeasonApi.toDomain(): Season = when (this) {
    SeasonApi.AUTUMN -> Season.AUTUMN
    SeasonApi.SPRING -> Season.SPRING
    SeasonApi.SUMMER -> Season.SUMMER
    SeasonApi.WINTER -> Season.WINTER
}

internal fun Season.toApi(): SeasonApi = when (this) {
    Season.AUTUMN -> SeasonApi.AUTUMN
    Season.SPRING -> SeasonApi.SPRING
    Season.SUMMER -> SeasonApi.SUMMER
    Season.WINTER -> SeasonApi.WINTER
}

internal fun SortingTypeApi.toDomain(): SortingType = when (this) {
    SortingTypeApi.YEAR_ASC -> SortingType.YEAR_ASC
    SortingTypeApi.YEAR_DESC -> SortingType.YEAR_DESC
    SortingTypeApi.RATING_ASC -> SortingType.RATING_ASC
    SortingTypeApi.RATING_DESC -> SortingType.RATING_DESC
    SortingTypeApi.FRESH_AT_ASC -> SortingType.FRESH_AT_ASC
    SortingTypeApi.FRESH_AT_DESC -> SortingType.FRESH_AT_DESC
}

internal fun SortingType.toApi(): SortingTypeApi = when (this) {
    SortingType.YEAR_ASC -> SortingTypeApi.YEAR_ASC
    SortingType.YEAR_DESC -> SortingTypeApi.YEAR_DESC
    SortingType.RATING_ASC -> SortingTypeApi.RATING_ASC
    SortingType.RATING_DESC -> SortingTypeApi.RATING_DESC
    SortingType.FRESH_AT_ASC -> SortingTypeApi.FRESH_AT_ASC
    SortingType.FRESH_AT_DESC -> SortingTypeApi.FRESH_AT_DESC
}
