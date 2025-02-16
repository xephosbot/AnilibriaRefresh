package com.xbot.search.utils

import androidx.annotation.StringRes
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.search.R

@get:StringRes
val AgeRating.stringRes: Int
    get() = when (this) {
        AgeRating.R0_PLUS -> R.string.age_rating_0_plus
        AgeRating.R6_PLUS -> R.string.age_rating_6_plus
        AgeRating.R12_PLUS -> R.string.age_rating_12_plus
        AgeRating.R16_PLUS -> R.string.age_rating_16_plus
        AgeRating.R18_PLUS -> R.string.age_rating_18_plus
    }

@get:StringRes
val ReleaseType.stringRes: Int
    get() = when (this) {
        ReleaseType.TV -> R.string.release_type_tv
        ReleaseType.ONA -> R.string.release_type_ona
        ReleaseType.WEB -> R.string.release_type_web
        ReleaseType.OVA -> R.string.release_type_ova
        ReleaseType.OAD -> R.string.release_type_oad
        ReleaseType.MOVIE -> R.string.release_type_movie
        ReleaseType.DORAMA -> R.string.release_type_dorama
        ReleaseType.SPECIAL -> R.string.release_type_special
    }

@get:StringRes
val ProductionStatus.stringRes: Int
    get() = when (this) {
        ProductionStatus.IS_IN_PRODUCTION -> R.string.production_status_is_in_production
        ProductionStatus.IS_NOT_IN_PRODUCTION -> R.string.production_status_is_not_in_production
    }

@get:StringRes
val PublishStatus.stringRes: Int
    get() = when (this) {
        PublishStatus.IS_ONGOING -> R.string.publish_status_is_ongoing
        PublishStatus.IS_NOT_ONGOING -> R.string.publish_status_is_not_ongoing
    }

@get:StringRes
val SortingType.stringRes: Int
    get() = when (this) {
        SortingType.FRESH_AT_DESC -> R.string.sorting_types_fresh_at_desc
        SortingType.FRESH_AT_ASC -> R.string.sorting_types_fresh_at_asc
        SortingType.RATING_DESC -> R.string.sorting_types_rating_desc
        SortingType.RATING_ASC -> R.string.sorting_types_rating_asc
        SortingType.YEAR_DESC -> R.string.sorting_types_year_desc
        SortingType.YEAR_ASC -> R.string.sorting_types_year_asc
    }

@get:StringRes
val Season.stringRes: Int
    get() = when (this) {
        Season.WINTER -> R.string.season_winter
        Season.SPRING -> R.string.season_spring
        Season.SUMMER -> R.string.season_summer
        Season.AUTUMN -> R.string.season_autumn
    }