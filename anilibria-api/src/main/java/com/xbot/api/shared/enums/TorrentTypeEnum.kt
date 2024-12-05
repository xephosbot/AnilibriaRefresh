package com.xbot.api.shared.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TorrentTypeEnum(private val value: String) {
    @SerialName("BDRip") BD_RIP("BDRip"),

    @SerialName("HDRip") HD_RIP("HDRip"),

    @SerialName("TVRip") TV_RIP("TVRip"),

    @SerialName("WEBRip") WEB_RIP("WEBRip"),

    @SerialName("DTVRip") DTV_RIP("DTVRip"),

    @SerialName("DVDRip") DVD_RIP("DVDRip"),

    @SerialName("HDTVRip") HDTV_RIP("HDTVRip"),

    @SerialName("WEB-DLRip") WEB_DL_RIP("WEB-DLRip"),
    ;

    override fun toString(): String = value
}
