package com.xbot.domain.models

data class ReleaseRating(
    val average: Double?,
    val votes: Int,
    val votesByScore: Map<Int, Int>
)

data class ExternalRating(
    val rating: Double,
    val votes: Int,
    val url: String
)
