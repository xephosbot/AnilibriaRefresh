package com.xbot.api.franchises.models

import com.xbot.api.shared.Release
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FranchiseRelease(
    @SerialName("id") val id: String,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("release_id") val releaseId: Int,
    @SerialName("franchise_id") val franchiseId: String,
    @SerialName("release") val release: com.xbot.api.shared.Release,
)