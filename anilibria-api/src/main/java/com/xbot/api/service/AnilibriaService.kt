package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.external.YouTubeVideo
import com.xbot.api.models.misc.FeedItem
import com.xbot.api.models.misc.Schedule
import com.xbot.api.models.misc.TitleUpdate
import com.xbot.api.models.title.Title
import retrofit2.http.GET
import retrofit2.http.Query

interface AnilibriaService {
    @GET("title")
    suspend fun getTitle(
        @Query("id") id: Int? = null,
        @Query("code") code: String? = null,
        @Query("torrent_id") torrentId: Int? = null,
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("include") include: String? = null,
        @Query("description_type") descriptionType: String? = "plain",
        @Query("playlist_type") playlistType: String? = "object"
    ): ApiResponse<Title>

    @GET("title/list")
    suspend fun getTitleList(
        @Query("id_list") idList: String? = null,
        @Query("code_list") codeList: String? = null,
        @Query("torrent_id_list") torrentIdList: String? = null,
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("include") include: String? = null,
        @Query("description_type") descriptionType: String? = "plain",
        @Query("playlist_type") playlistType: String? = "object",
        @Query("page") page: Int? = null,
        @Query("items_per_page") itemsPerPage: Int? = null
    ): ApiResponse<List<Title>>

    @GET("title/updates")
    suspend fun getTitleUpdates(
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("limit") limit: Int? = 5,
        @Query("since") since: Int? = null,
        @Query("description_type") descriptionType: String? = "plain",
        @Query("playlist_type") playlistType: String? = "object",
        @Query("page") page: Int? = null,
        @Query("items_per_page") itemsPerPage: Int? = null
    ): ApiResponse<TitleUpdate>

    @GET("title/changes")
    suspend fun getTitleChanges(
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("limit") limit: Int? = 5,
        @Query("since") since: Int? = null,
        @Query("description_type") descriptionType: String? = "plain",
        @Query("playlist_type") playlistType: String? = "object",
        @Query("page") page: Int? = null,
        @Query("items_per_page") itemsPerPage: Int? = null
    ): ApiResponse<TitleUpdate>

    @GET("title/schedule")
    suspend fun getTitleSchedule(
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("days") days: String? = null,
        @Query("description_type") descriptionType: String? = "plain",
        @Query("playlist_type") playlistType: String? = "object"
    ): ApiResponse<List<Schedule>>

    @GET("title/random")
    suspend fun getRandomTitle(
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("include") include: String? = null,
        @Query("description_type") descriptionType: String? = "plain",
        @Query("playlist_type") playlistType: String? = "object"
    ): ApiResponse<Title>

    @GET("youtube")
    suspend fun getYouTubeVideos(
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("limit") limit: Int? = 5,
        @Query("since") since: Int? = null,
        @Query("after") after: Int? = null,
        @Query("page") page: Int? = null,
        @Query("items_per_page") itemsPerPage: Int? = null
    ): ApiResponse<List<YouTubeVideo>>

    @GET("feed")
    suspend fun getFeed(
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("include") include: String? = null,
        @Query("limit") limit: Int? = 5,
        @Query("since") since: Int? = null,
        @Query("after") after: Int? = null,
        @Query("page") page: Int? = null,
        @Query("items_per_page") itemsPerPage: Int? = null
    ): ApiResponse<List<FeedItem>>

    @GET("title/search")
    suspend fun searchTitles(
        @Query("search") search: String? = null,
        @Query("year") year: String? = null,
        @Query("type") type: String? = null,
        @Query("season_code") seasonCode: String? = null,
        @Query("genres") genres: String? = null,
        @Query("team") team: String? = null,
        @Query("voice") voice: String? = null,
        @Query("translator") translator: String? = null,
        @Query("editing") editing: String? = null,
        @Query("decor") decor: String? = null,
        @Query("timing") timing: String? = null,
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("include") include: String? = null,
        @Query("description_type") descriptionType: String = "plain",
        @Query("playlist_type") playlistType: String = "object",
        @Query("limit") limit: Int = 5,
        @Query("after") after: Int? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort_direction") sortDirection: Int = 0,
        @Query("page") page: Int? = null,
        @Query("items_per_page") itemsPerPage: Int? = null
    ): ApiResponse<List<Title>>

    @GET("title/search/advanced")
    fun searchTitlesAdvanced(
        @Query("query") query: String,
        @Query("simple_query") simpleQuery: String? = null,
        @Query("filter") filter: String? = null,
        @Query("remove") remove: String? = null,
        @Query("include") include: String? = null,
        @Query("description_type") descriptionType: String = "plain",
        @Query("playlist_type") playlistType: String = "object",
        @Query("limit") limit: Int = 5,
        @Query("after") after: Int? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort_direction") sortDirection: Int = 0,
        @Query("page") page: Int? = null,
        @Query("items_per_page") itemsPerPage: Int? = null
    ): ApiResponse<List<Title>>

    companion object {
        const val BASE_URL: String = "https://api.anilibria.tv/v3/"
    }
}