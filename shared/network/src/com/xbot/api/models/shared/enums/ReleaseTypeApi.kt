package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ReleaseTypeApi.Companion.Serializer::class)
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

    companion object {
        object Serializer : KSerializer<ReleaseTypeApi?> by EnumSerializer.create<ReleaseTypeApi>()
    }
}
