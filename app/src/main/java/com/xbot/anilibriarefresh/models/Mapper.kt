package com.xbot.anilibriarefresh.models

import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingTypes

fun TitleModel.toTitleUi(): Title {
    return Title(
        id = id,
        name = name,
        description = description,
        tags = listOfTags(),
        poster = Poster(poster.src),
    )
}

fun TitleDetailModel.toTitleDetailUi(): TitleDetail {
    return TitleDetail(
        id = id,
        name = name,
        poster = Poster(poster.src),
        description = description,
        tags = listOf(),
        notification = notification,
        genres = genres,
        members = members,
        episodes = episodes,
    )
}

// TODO: CLean up after migrate
fun PosterModel.toPosterUi(): Poster {
    return Poster(src)
}

private fun TitleModel.listOfTags(): List<TitleTag> {
    return buildList {
        add(TitleTag.Text(StringResource.String(year.toString())))
        if (type != ReleaseType.MOVIE && episodesCount != null) {
            add(TitleTag.Text(StringResource.Text(R.string.episodes_count_title, episodesCount!!)))
        } else if (type != null && type == ReleaseType.MOVIE) {
            add(TitleTag.Text(type!!.toStringResource()))
        }
        add(TitleTag.TextWithIcon(StringResource.String(favoritesCount.toString()), AnilibriaIcons.Filled.Heart))
    }
}

fun ReleaseType.toStringResource(): StringResource {
    val resId = when (this) {
        ReleaseType.TV -> R.string.release_type_tv
        ReleaseType.ONA -> R.string.release_type_ona
        ReleaseType.WEB -> R.string.release_type_web
        ReleaseType.OVA -> R.string.release_type_ova
        ReleaseType.OAD -> R.string.release_type_oad
        ReleaseType.MOVIE -> R.string.release_type_movie
        ReleaseType.DORAMA -> R.string.release_type_dorama
        ReleaseType.SPECIAL -> R.string.release_type_special
    }
    return StringResource.Text(resId)
}

fun AgeRating.toStringResource(): StringResource {
    val resId = when (this) {
        AgeRating.R0_PLUS -> R.string.age_rating_0_plus
        AgeRating.R6_PLUS -> R.string.age_rating_6_plus
        AgeRating.R12_PLUS -> R.string.age_rating_12_plus
        AgeRating.R16_PLUS -> R.string.age_rating_16_plus
        AgeRating.R18_PLUS -> R.string.age_rating_18_plus
    }
    return StringResource.Text(resId)
}

fun ProductionStatus.toStringResource(): StringResource {
    val resId = when (this) {
        ProductionStatus.IS_IN_PRODUCTION -> R.string.production_status_is_in_production
        ProductionStatus.IS_NOT_IN_PRODUCTION -> R.string.production_status_is_not_in_production
    }
    return StringResource.Text(resId)
}

fun PublishStatus.toStringResource(): StringResource {
    val resId = when (this) {
        PublishStatus.IS_ONGOING -> R.string.publish_status_is_ongoing
        PublishStatus.IS_NOT_ONGOING -> R.string.publish_status_is_not_ongoing
    }
    return StringResource.Text(resId)
}

fun Season.toStringResource(): StringResource {
    val resId = when (this) {
        Season.WINTER -> R.string.season_winter
        Season.SPRING -> R.string.season_spring
        Season.SUMMER -> R.string.season_summer
        Season.AUTUMN -> R.string.season_autumn
    }
    return StringResource.Text(resId)
}

fun SortingTypes.toStringResource(): StringResource {
    val resId = when (this) {
        SortingTypes.FRESH_AT_DESC -> R.string.sorting_types_fresh_at_desc
        SortingTypes.FRESH_AT_ASC -> R.string.sorting_types_fresh_at_asc
        SortingTypes.RATING_DESC -> R.string.sorting_types_rating_desc
        SortingTypes.RATING_ASC -> R.string.sorting_types_rating_asc
        SortingTypes.YEAR_DESC -> R.string.sorting_types_year_desc
        SortingTypes.YEAR_ASC -> R.string.sorting_types_year_asc
    }
    return StringResource.Text(resId)
}
