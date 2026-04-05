package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = TorrentTypeDto.Companion.Serializer::class)
enum class TorrentTypeDto(val value: String) {
    @SerialName("BDRip") BD_RIP("BDRip"),
    @SerialName("HDRip") HD_RIP("HDRip"),
    @SerialName("TVRip") TV_RIP("TVRip"),
    @SerialName("WEBRip") WEB_RIP("WEBRip"),
    @SerialName("DTVRip") DTV_RIP("DTVRip"),
    @SerialName("DVDRip") DVD_RIP("DVDRip"),
    @SerialName("HDTVRip") HDTV_RIP("HDTVRip"),
    @SerialName("WEB-DL") WEB_DL("WEB-DL"),
    @SerialName("WEB-DLRip") WEB_DL_RIP("WEB-DLRip");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<TorrentTypeDto?> by EnumSerializer.create<TorrentTypeDto>()
    }
}
