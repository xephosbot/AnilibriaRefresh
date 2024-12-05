package com.xbot.api.franchises.models

import com.xbot.api.shared.Image
import com.xbot.api.shared.Release
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Franchise(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("name_english") val nameEnglish: String,
    @SerialName("rating") val rating: Double,
    @SerialName("last_year") val lastYear: Int,
    @SerialName("first_year") val firstYear: Int,
    @SerialName("total_releases") val totalReleases: Int,
    @SerialName("total_episodes") val totalEpisodes: Int,
    @SerialName("total_duration") val totalDuration: String,
    @SerialName("total_duration_in_seconds") val totalDurationInSeconds: Long,
    @SerialName("image") val image: com.xbot.api.shared.Image,
    @SerialName("franchise_releases") val franchiseReleases: List<FranchiseRelease>? = null,
)