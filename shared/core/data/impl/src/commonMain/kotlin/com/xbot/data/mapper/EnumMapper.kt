package com.xbot.data.mapper

import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.MemberRoleDto
import com.xbot.network.models.enums.ProductionStatusDto
import com.xbot.network.models.enums.PublishDayDto
import com.xbot.network.models.enums.PublishStatusDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SeasonDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.MemberRole
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SocialType
import com.xbot.domain.models.enums.SortingType
import com.xbot.network.models.enums.SocialTypeDto
import kotlinx.datetime.DayOfWeek

internal fun AgeRatingDto.toDomain(): AgeRating = when (this) {
    AgeRatingDto.R0_PLUS -> AgeRating.R0_PLUS
    AgeRatingDto.R6_PLUS -> AgeRating.R6_PLUS
    AgeRatingDto.R12_PLUS -> AgeRating.R12_PLUS
    AgeRatingDto.R16_PLUS -> AgeRating.R16_PLUS
    AgeRatingDto.R18_PLUS -> AgeRating.R18_PLUS
}

internal fun AgeRating.toDto(): AgeRatingDto = when (this) {
    AgeRating.R0_PLUS -> AgeRatingDto.R0_PLUS
    AgeRating.R6_PLUS -> AgeRatingDto.R6_PLUS
    AgeRating.R12_PLUS -> AgeRatingDto.R12_PLUS
    AgeRating.R16_PLUS -> AgeRatingDto.R16_PLUS
    AgeRating.R18_PLUS -> AgeRatingDto.R18_PLUS
}

internal fun ProductionStatusDto.toDomain(): ProductionStatus = when (this) {
    ProductionStatusDto.IS_IN_PRODUCTION -> ProductionStatus.IS_IN_PRODUCTION
    ProductionStatusDto.IS_NOT_IN_PRODUCTION -> ProductionStatus.IS_NOT_IN_PRODUCTION
}

internal fun ProductionStatus.toDto(): ProductionStatusDto = when (this) {
    ProductionStatus.IS_IN_PRODUCTION -> ProductionStatusDto.IS_IN_PRODUCTION
    ProductionStatus.IS_NOT_IN_PRODUCTION -> ProductionStatusDto.IS_NOT_IN_PRODUCTION
}

internal fun PublishStatusDto.toDomain(): PublishStatus = when (this) {
    PublishStatusDto.IS_ONGOING -> PublishStatus.IS_ONGOING
    PublishStatusDto.IS_NOT_ONGOING -> PublishStatus.IS_NOT_ONGOING
}

internal fun PublishStatus.toDto(): PublishStatusDto = when (this) {
    PublishStatus.IS_ONGOING -> PublishStatusDto.IS_ONGOING
    PublishStatus.IS_NOT_ONGOING -> PublishStatusDto.IS_NOT_ONGOING
}

internal fun ReleaseTypeDto.toDomain(): ReleaseType = when (this) {
    ReleaseTypeDto.OAD -> ReleaseType.OAD
    ReleaseTypeDto.ONA -> ReleaseType.ONA
    ReleaseTypeDto.OVA -> ReleaseType.OVA
    ReleaseTypeDto.WEB -> ReleaseType.WEB
    ReleaseTypeDto.DORAMA -> ReleaseType.DORAMA
    ReleaseTypeDto.SPECIAL -> ReleaseType.SPECIAL
    ReleaseTypeDto.MOVIE -> ReleaseType.MOVIE
    ReleaseTypeDto.TV -> ReleaseType.TV
}

internal fun ReleaseType.toDto(): ReleaseTypeDto = when (this) {
    ReleaseType.OAD -> ReleaseTypeDto.OAD
    ReleaseType.ONA -> ReleaseTypeDto.ONA
    ReleaseType.OVA -> ReleaseTypeDto.OVA
    ReleaseType.WEB -> ReleaseTypeDto.WEB
    ReleaseType.DORAMA -> ReleaseTypeDto.DORAMA
    ReleaseType.SPECIAL -> ReleaseTypeDto.SPECIAL
    ReleaseType.MOVIE -> ReleaseTypeDto.MOVIE
    ReleaseType.TV -> ReleaseTypeDto.TV
}

internal fun PublishDayDto.toDayOfWeek(): DayOfWeek = when (this) {
    PublishDayDto.MONDAY -> DayOfWeek.MONDAY
    PublishDayDto.TUESDAY -> DayOfWeek.TUESDAY
    PublishDayDto.WEDNESDAY -> DayOfWeek.WEDNESDAY
    PublishDayDto.THURSDAY -> DayOfWeek.THURSDAY
    PublishDayDto.FRIDAY -> DayOfWeek.FRIDAY
    PublishDayDto.SATURDAY -> DayOfWeek.SATURDAY
    PublishDayDto.SUNDAY -> DayOfWeek.SUNDAY
}

internal fun SeasonDto.toDomain(): Season = when (this) {
    SeasonDto.AUTUMN -> Season.AUTUMN
    SeasonDto.SPRING -> Season.SPRING
    SeasonDto.SUMMER -> Season.SUMMER
    SeasonDto.WINTER -> Season.WINTER
}

internal fun Season.toDto(): SeasonDto = when (this) {
    Season.AUTUMN -> SeasonDto.AUTUMN
    Season.SPRING -> SeasonDto.SPRING
    Season.SUMMER -> SeasonDto.SUMMER
    Season.WINTER -> SeasonDto.WINTER
}

internal fun SortingTypeDto.toDomain(): SortingType = when (this) {
    SortingTypeDto.YEAR_ASC -> SortingType.YEAR_ASC
    SortingTypeDto.YEAR_DESC -> SortingType.YEAR_DESC
    SortingTypeDto.RATING_ASC -> SortingType.RATING_ASC
    SortingTypeDto.RATING_DESC -> SortingType.RATING_DESC
    SortingTypeDto.FRESH_AT_ASC -> SortingType.FRESH_AT_ASC
    SortingTypeDto.FRESH_AT_DESC -> SortingType.FRESH_AT_DESC
}

internal fun SortingType.toDto(): SortingTypeDto = when (this) {
    SortingType.YEAR_ASC -> SortingTypeDto.YEAR_ASC
    SortingType.YEAR_DESC -> SortingTypeDto.YEAR_DESC
    SortingType.RATING_ASC -> SortingTypeDto.RATING_ASC
    SortingType.RATING_DESC -> SortingTypeDto.RATING_DESC
    SortingType.FRESH_AT_ASC -> SortingTypeDto.FRESH_AT_ASC
    SortingType.FRESH_AT_DESC -> SortingTypeDto.FRESH_AT_DESC
}

internal fun MemberRoleDto.toDomain(): MemberRole = when (this) {
    MemberRoleDto.POSTER -> MemberRole.POSTER
    MemberRoleDto.TIMING -> MemberRole.TIMING
    MemberRoleDto.VOICING -> MemberRole.VOICING
    MemberRoleDto.EDITING -> MemberRole.EDITING
    MemberRoleDto.DECORATING -> MemberRole.DECORATING
    MemberRoleDto.TRANSLATING -> MemberRole.TRANSLATING
}

internal fun SocialType.toDto(): SocialTypeDto = when (this) {
    SocialType.VK -> SocialTypeDto.VK
    SocialType.GOOGLE -> SocialTypeDto.GOOGLE
    SocialType.PATREON -> SocialTypeDto.PATREON
    SocialType.DISCORD -> SocialTypeDto.DISCORD
}