package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object ReleaseTypeEnumSerializer
    : EnumSerializer<ReleaseTypeApi>(ReleaseTypeApi.entries.toTypedArray(), ReleaseTypeApi::value)

@Serializable(with = ReleaseTypeEnumSerializer::class)
enum class ReleaseTypeApi(val value: String) {
    @SerialName("TV") TV("TV"),
    @SerialName("ONA") ONA("ONA"),
    @SerialName("WEB") WEB("WEB"),
    @SerialName("OVA") OVA("OVA"),
    @SerialName("OAD") OAD("OAD"),
    @SerialName("MOVIE") MOVIE("MOVIE"),
    @SerialName("DORAMA") DORAMA("DORAMA"),
    @SerialName("SPECIAL") SPECIAL("SPECIAL"), ;

    override fun toString(): String = value
}
