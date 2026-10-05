package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppStatusDto(
    @SerialName("request") val request: Request? = null,
    @SerialName("is_alive") val isAlive: Boolean,
    @SerialName("available_api_endpoints") val availableApiEndpoints: List<String> = emptyList()
) {
    @Serializable
    data class Request(
        @SerialName("ip") val ip: String? = null,
        @SerialName("country") val country: String? = null,
        @SerialName("iso_code") val isoCode: String? = null,
        @SerialName("timezone") val timezone: String? = null
    )
}
