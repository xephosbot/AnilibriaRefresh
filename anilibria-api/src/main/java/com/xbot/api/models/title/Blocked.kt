package com.xbot.api.models.title

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Blocked(
    @SerialName("copyrights") val copyrights: Boolean,
    @SerialName("geoip") val geoIp: Boolean,
    //TODO: unkown field
    @SerialName("geoip_list") val geoIpList: List<Unit>
)