package com.xbot.data.mapper

import com.xbot.api.models.shared.enums.AgeRatingEnum
import com.xbot.api.models.shared.enums.ProductionStatusEnum
import com.xbot.api.models.shared.enums.PublishDayEnum
import com.xbot.api.models.shared.enums.PublishStatusEnum
import com.xbot.api.models.shared.enums.ReleaseTypeEnum
import com.xbot.api.models.shared.enums.SeasonEnum
import com.xbot.api.models.shared.enums.SortingTypeEnum
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

internal fun AgeRatingEnum.toAgeRating(): AgeRating = when (this) {
    AgeRatingEnum.R0_PLUS -> AgeRating.R0_PLUS
    AgeRatingEnum.R6_PLUS -> AgeRating.R6_PLUS
    AgeRatingEnum.R12_PLUS -> AgeRating.R12_PLUS
    AgeRatingEnum.R16_PLUS -> AgeRating.R16_PLUS
    AgeRatingEnum.R18_PLUS -> AgeRating.R18_PLUS
}

internal fun ProductionStatusEnum.toProductionStatus(): ProductionStatus = when (this) {
    ProductionStatusEnum.IS_IN_PRODUCTION -> ProductionStatus.IS_IN_PRODUCTION
    ProductionStatusEnum.IS_NOT_IN_PRODUCTION -> ProductionStatus.IS_NOT_IN_PRODUCTION
}

internal fun PublishStatusEnum.toPublishStatus(): PublishStatus = when (this) {
    PublishStatusEnum.IS_ONGOING -> PublishStatus.IS_ONGOING
    PublishStatusEnum.IS_NOT_ONGOING -> PublishStatus.IS_NOT_ONGOING
}

internal fun ReleaseTypeEnum.toReleaseType(): ReleaseType = when (this) {
    ReleaseTypeEnum.OAD -> ReleaseType.OAD
    ReleaseTypeEnum.ONA -> ReleaseType.ONA
    ReleaseTypeEnum.OVA -> ReleaseType.OVA
    ReleaseTypeEnum.WEB -> ReleaseType.WEB
    ReleaseTypeEnum.DORAMA -> ReleaseType.DORAMA
    ReleaseTypeEnum.SPECIAL -> ReleaseType.SPECIAL
    ReleaseTypeEnum.MOVIE -> ReleaseType.MOVIE
    ReleaseTypeEnum.TV -> ReleaseType.TV
}

internal fun PublishDayEnum.toDayOfWeek(): DayOfWeek = when (this) {
    PublishDayEnum.MONDAY -> DayOfWeek.MONDAY
    PublishDayEnum.TUESDAY -> DayOfWeek.TUESDAY
    PublishDayEnum.WEDNESDAY -> DayOfWeek.WEDNESDAY
    PublishDayEnum.THURSDAY -> DayOfWeek.THURSDAY
    PublishDayEnum.FRIDAY -> DayOfWeek.FRIDAY
    PublishDayEnum.SATURDAY -> DayOfWeek.SATURDAY
    PublishDayEnum.SUNDAY -> DayOfWeek.SUNDAY
}

internal fun SeasonEnum.toSeason(): Season = when (this) {
    SeasonEnum.AUTUMN -> Season.AUTUMN
    SeasonEnum.SPRING -> Season.SPRING
    SeasonEnum.SUMMER -> Season.SUMMER
    SeasonEnum.WINTER -> Season.WINTER
}

internal fun SortingTypeEnum.toSortingType(): SortingType = when (this) {
    SortingTypeEnum.YEAR_ASC -> SortingType.YEAR_ASC
    SortingTypeEnum.YEAR_DESC -> SortingType.YEAR_DESC
    SortingTypeEnum.RATING_ASC -> SortingType.RATING_ASC
    SortingTypeEnum.RATING_DESC -> SortingType.RATING_DESC
    SortingTypeEnum.FRESH_AT_ASC -> SortingType.FRESH_AT_ASC
    SortingTypeEnum.FRESH_AT_DESC -> SortingType.FRESH_AT_DESC
}
