package com.xbot.api.models.franchises

import com.xbot.api.models.shared.ReleaseApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FranchiseReleaseApi(
    @SerialName("id") val id: String,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("release_id") val releaseId: Int,
    @SerialName("franchise_id") val franchiseId: String,
    @SerialName("release") val release: ReleaseApi,
)