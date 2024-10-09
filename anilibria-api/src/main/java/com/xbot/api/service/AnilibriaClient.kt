package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.misc.TitleUpdate
import com.xbot.api.models.title.Title
import javax.inject.Inject

class AnilibriaClient @Inject constructor(
    private val service: AnilibriaService
) {
    suspend fun getTitle(
        id: Int,
        code: String? = null,
        torrentId: Int? = null,
        filter: List<String>? = null,
        remove: List<String>? = null,
        include: List<String>? = null,
        descriptionType: DescriptionType = DescriptionType.PLAIN,
        playlistType: PlaylistType = PlaylistType.OBJECT
    ): ApiResponse<Title> {
        return service.getTitle(
            id = id,
            code = code,
            torrentId = torrentId,
            filter = filter?.joinToString(","),
            remove = remove?.joinToString(","),
            include = include?.joinToString(","),
            descriptionType = descriptionType.toString(),
            playlistType = playlistType.toString()
        )
    }

    suspend fun getTitles(
        idList: List<Int>,
        codeList: List<String>? = null,
        torrentIdList: List<String>? = null,
        filter: List<String>? = null,
        remove: List<String>? = null,
        include: List<String>? = null,
        descriptionType: DescriptionType = DescriptionType.PLAIN,
        playlistType: PlaylistType = PlaylistType.OBJECT,
        page: Int? = null,
        itemsPerPage: Int? = null
    ): ApiResponse<List<Title>> {
        return service.getTitleList(
            idList = idList.joinToString(","),
            codeList = codeList?.joinToString(","),
            torrentIdList = torrentIdList?.joinToString(","),
            filter = filter?.joinToString(","),
            remove = remove?.joinToString(","),
            include = include?.joinToString(","),
            descriptionType = descriptionType.toString(),
            playlistType = playlistType.toString(),
            page = page,
            itemsPerPage = itemsPerPage
        )
    }

    suspend fun getTitleUpdates(
        filter: List<String>? = null,
        remove: List<String>? = null,
        limit: Int? = 5,
        since: Int? = null,
        descriptionType: DescriptionType = DescriptionType.PLAIN,
        playlistType: PlaylistType = PlaylistType.OBJECT,
        page: Int? = null,
        itemsPerPage: Int? = null
    ): ApiResponse<TitleUpdate> {
        return service.getTitleUpdates(
            filter = filter?.joinToString(","),
            remove = remove?.joinToString(","),
            limit = limit,
            since = since,
            descriptionType = descriptionType.toString(),
            playlistType = playlistType.toString(),
            page = page,
            itemsPerPage = itemsPerPage
        )
    }

    enum class DescriptionType(private val type: String) {
        HTML("html"),
        PLAIN("plain"),
        NO_VIEW_ORDER("no_view_order");

        override fun toString(): String {
            return type
        }
    }

    enum class PlaylistType(private val type: String) {
        OBJECT("object"),
        ARRAY("array");

        override fun toString(): String {
            return type
        }
    }
}