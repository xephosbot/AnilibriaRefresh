package com.xbot.anilibriarefresh.models

import com.xbot.domain.models.EpisodeModel
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.MemberModel

data class TitleDetail(
    val id: Int,
    val name: String,
    val poster: Poster,
    val description: String,
    val tags: List<TitleTag>,
    val notification: String,
    val genres: List<GenreModel>,
    val members: List<MemberModel>,
    val episodes: List<EpisodeModel>,
)
