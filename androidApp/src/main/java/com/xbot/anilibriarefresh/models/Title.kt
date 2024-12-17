package com.xbot.anilibriarefresh.models

import androidx.compose.runtime.Stable

@Stable
data class Title(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<TitleTag>,
    val poster: Poster,
)
