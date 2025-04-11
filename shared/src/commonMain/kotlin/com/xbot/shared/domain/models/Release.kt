package com.xbot.shared.domain.models

import com.xbot.shared.domain.models.enums.AgeRating
import com.xbot.shared.domain.models.enums.ReleaseType

data class Release(
    val id: Int,
    val type: ReleaseType?,
    val year: Int,
    val name: String,
    val englishName: String,
    val description: String?,
    val ageRating: AgeRating,
    val episodesCount: Int?,
    val episodeDuration: Int?,
    val favoritesCount: Int,
    val poster: Poster?,
)
