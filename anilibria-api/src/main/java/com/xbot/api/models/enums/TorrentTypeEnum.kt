package com.xbot.api.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TorrentTypeEnum(private val value: String) {
    @SerialName("BDRip") BDRip("BDRip"),
    @SerialName("HDRip") HDRip("HDRip"),
    @SerialName("TVRip") TVRip("TVRip"),
    @SerialName("WEBRip") WEBRip("WEBRip"),
    @SerialName("DTVRip") DTVRip("DTVRip"),
    @SerialName("DVDRip") DVDRip("DVDRip"),
    @SerialName("HDTVRip") HDTVRip("HDTVRip"),
    @SerialName("WEB-DLRip") WEB_DLRip("WEB-DLRip");

    override fun toString(): String = value
}