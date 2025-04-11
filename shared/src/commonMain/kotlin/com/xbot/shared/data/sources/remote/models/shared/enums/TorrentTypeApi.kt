package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object TorrentTypeEnumSerializer
    : EnumSerializer<TorrentTypeApi>(TorrentTypeApi.entries.toTypedArray(), TorrentTypeApi::value)

@Serializable(with = TorrentTypeEnumSerializer::class)
enum class TorrentTypeApi(val value: String) {
    @SerialName("BDRip") BD_RIP("BDRip"),
    @SerialName("HDRip") HD_RIP("HDRip"),
    @SerialName("TVRip") TV_RIP("TVRip"),
    @SerialName("WEBRip") WEB_RIP("WEBRip"),
    @SerialName("DTVRip") DTV_RIP("DTVRip"),
    @SerialName("DVDRip") DVD_RIP("DVDRip"),
    @SerialName("HDTVRip") HDTV_RIP("HDTVRip"),
    @SerialName("WEB-DLRip") WEB_DL_RIP("WEB-DLRip");

    override fun toString(): String = value
}
