package com.xbot.localization

import com.xbot.domain.models.enums.*
import com.xbot.resources.*
import org.jetbrains.compose.resources.StringResource

val AgeRating.stringRes: StringResource
    get() = when (this) {
        AgeRating.R0_PLUS -> Res.string.age_rating_0_plus
        AgeRating.R6_PLUS -> Res.string.age_rating_6_plus
        AgeRating.R12_PLUS -> Res.string.age_rating_12_plus
        AgeRating.R16_PLUS -> Res.string.age_rating_16_plus
        AgeRating.R18_PLUS -> Res.string.age_rating_18_plus
    }

val ReleaseType.stringRes: StringResource
    get() = when (this) {
        ReleaseType.TV -> Res.string.release_type_tv
        ReleaseType.ONA -> Res.string.release_type_ona
        ReleaseType.WEB -> Res.string.release_type_web
        ReleaseType.OVA -> Res.string.release_type_ova
        ReleaseType.OAD -> Res.string.release_type_oad
        ReleaseType.MOVIE -> Res.string.release_type_movie
        ReleaseType.DORAMA -> Res.string.release_type_dorama
        ReleaseType.SPECIAL -> Res.string.release_type_special
    }

val ProductionStatus.stringRes: StringResource
    get() = when (this) {
        ProductionStatus.IS_IN_PRODUCTION -> Res.string.production_status_is_in_production
        ProductionStatus.IS_NOT_IN_PRODUCTION -> Res.string.production_status_is_not_in_production
    }

val PublishStatus.stringRes: StringResource
    get() = when (this) {
        PublishStatus.IS_ONGOING -> Res.string.publish_status_is_ongoing
        PublishStatus.IS_NOT_ONGOING -> Res.string.publish_status_is_not_ongoing
    }

val SortingType.stringRes: StringResource
    get() = when (this) {
        SortingType.FRESH_AT_DESC -> Res.string.sorting_types_fresh_at_desc
        SortingType.FRESH_AT_ASC -> Res.string.sorting_types_fresh_at_asc
        SortingType.RATING_DESC -> Res.string.sorting_types_rating_desc
        SortingType.RATING_ASC -> Res.string.sorting_types_rating_asc
        SortingType.YEAR_DESC -> Res.string.sorting_types_year_desc
        SortingType.YEAR_ASC -> Res.string.sorting_types_year_asc
    }

val Season.stringRes: StringResource
    get() = when (this) {
        Season.WINTER -> Res.string.season_winter
        Season.SPRING -> Res.string.season_spring
        Season.SUMMER -> Res.string.season_summer
        Season.AUTUMN -> Res.string.season_autumn
    }

val MemberRole.stringRes: StringResource
    get() = when (this) {
        MemberRole.POSTER -> Res.string.member_role_poster
        MemberRole.TIMING -> Res.string.member_role_timing
        MemberRole.VOICING -> Res.string.member_role_voicing
        MemberRole.EDITING -> Res.string.member_role_editing
        MemberRole.DECORATING -> Res.string.member_role_decorating
        MemberRole.TRANSLATING -> Res.string.member_role_translating
    }

val ThemeOption.stringRes: StringResource
    get() = when (this) {
        ThemeOption.System -> Res.string.preference_appearance_theme_system
        ThemeOption.Dark -> Res.string.preference_appearance_theme_dark
        ThemeOption.Light -> Res.string.preference_appearance_theme_light
    }