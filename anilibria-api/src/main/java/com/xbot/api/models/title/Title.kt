package com.xbot.api.models.title

import com.xbot.api.models.common.Names
import com.xbot.api.models.common.Team
import com.xbot.api.models.media.Player
import com.xbot.api.models.media.Posters
import com.xbot.api.models.torrents.Torrents
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Title(
    @SerialName("id") val id: Int,
    @SerialName("code") val code: String? = null,
    @SerialName("names") val names: Names,
    @SerialName("franchises") val franchises: List<Franchise>? = null,
    @SerialName("announce") val announce: String? = null,
    @SerialName("status") val status: Status? = null,
    @SerialName("posters") val posters: Posters? = null,
    @SerialName("updated") val updated: Long? = null,
    @SerialName("last_change") val lastChange: Long? = null,
    @SerialName("type") val type: Type? = null,
    @SerialName("genres") val genres: List<String>? = null,
    @SerialName("team") val team: Team? = null,
    @SerialName("season") val season: Season? = null,
    @SerialName("description") val description: String?,
    @SerialName("in_favorites") val inFavorites: Int? = null,
    @SerialName("blocked") val blocked: Blocked? = null,
    @SerialName("player") val player: Player? = null,
    @SerialName("torrents") val torrents: Torrents? = null
)
