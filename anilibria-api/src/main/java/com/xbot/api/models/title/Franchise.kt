package com.xbot.api.models.title

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Franchise(
    @SerialName("franchise") val franchise: FranchiseDetail,
    @SerialName("releases") val releases: List<Release>
)
