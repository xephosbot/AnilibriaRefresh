package com.xbot.formatters

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.MemberRole
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.localization.AppLanguage
import com.xbot.resources.Res
import com.xbot.resources.age_rating_0_plus
import com.xbot.resources.age_rating_12_plus
import com.xbot.resources.age_rating_16_plus
import com.xbot.resources.age_rating_18_plus
import com.xbot.resources.age_rating_6_plus
import com.xbot.resources.member_role_decorating
import com.xbot.resources.member_role_editing
import com.xbot.resources.member_role_poster
import com.xbot.resources.member_role_timing
import com.xbot.resources.member_role_translating
import com.xbot.resources.member_role_voicing
import com.xbot.resources.preference_appearance_theme_dark
import com.xbot.resources.preference_appearance_theme_light
import com.xbot.resources.preference_appearance_theme_system
import com.xbot.resources.preference_language_en
import com.xbot.resources.preference_language_ru
import com.xbot.resources.production_status_is_in_production
import com.xbot.resources.production_status_is_not_in_production
import com.xbot.resources.publish_status_is_not_ongoing
import com.xbot.resources.publish_status_is_ongoing
import com.xbot.resources.release_type_dorama
import com.xbot.resources.release_type_movie
import com.xbot.resources.release_type_oad
import com.xbot.resources.release_type_ona
import com.xbot.resources.release_type_ova
import com.xbot.resources.release_type_special
import com.xbot.resources.release_type_tv
import com.xbot.resources.release_type_web
import com.xbot.resources.season_autumn
import com.xbot.resources.season_spring
import com.xbot.resources.season_summer
import com.xbot.resources.season_winter
import com.xbot.resources.sorting_types_fresh_at_asc
import com.xbot.resources.sorting_types_fresh_at_desc
import com.xbot.resources.sorting_types_rating_asc
import com.xbot.resources.sorting_types_rating_desc
import com.xbot.resources.sorting_types_year_asc
import com.xbot.resources.sorting_types_year_desc
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

val AppLanguage.stringRes: StringResource
    get() = when (this) {
        AppLanguage.English -> Res.string.preference_language_en
        AppLanguage.Russian -> Res.string.preference_language_ru
    }
