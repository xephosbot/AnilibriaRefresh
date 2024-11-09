package com.xbot.anilibriarefresh.models

import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.ReleaseType

fun TitleModel.toTitleUi(): Title {
    return Title(
        id = id,
        name = name,
        description = description,
        tags = listOfTags(),
        poster = Poster(poster.src)
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
        episodes = episodes
    )
}

//TODO: CLean up after migrate
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

private fun ReleaseType.toStringResource(): StringResource {
    val resId = when(this) {
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