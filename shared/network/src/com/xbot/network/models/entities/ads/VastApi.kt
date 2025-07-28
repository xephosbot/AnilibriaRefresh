package com.xbot.network.models.entities.ads

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VastApi(
    @SerialName("id") val id: String,
    @SerialName("url") val url: String,
    @SerialName("ad_erid") val adErid: String,
    @SerialName("ad_company_itn") val adCompanyItn: String,
    @SerialName("ad_company_name") val adCompanyName: String,
)
