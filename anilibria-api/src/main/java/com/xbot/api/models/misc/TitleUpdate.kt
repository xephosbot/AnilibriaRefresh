package com.xbot.api.models.misc

import com.xbot.api.models.title.Title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleUpdate(
    @SerialName("list") val list: List<Title>,
    @SerialName("pagination") val pagination: Pagination
)
