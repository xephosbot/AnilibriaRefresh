package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ReleaseTypeDto.Companion.Serializer::class)
enum class ReleaseTypeDto(val value: String) {
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
        object Serializer : KSerializer<ReleaseTypeDto?> by EnumSerializer.create<ReleaseTypeDto>()
    }
}
