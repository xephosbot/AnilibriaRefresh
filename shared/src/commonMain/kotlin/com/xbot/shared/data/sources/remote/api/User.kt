package com.xbot.shared.data.sources.remote.api

import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.models.account.AddTimeCodeRequest
import com.xbot.shared.data.sources.remote.models.account.AddToCollectionRequest
import com.xbot.shared.data.sources.remote.models.account.AddToFavoriteRequest
import com.xbot.shared.data.sources.remote.models.account.DeleteTimeCodeRequest
import com.xbot.shared.data.sources.remote.models.account.EpisodeWithTimeCode
import com.xbot.shared.data.sources.remote.models.account.ProfileApi
import com.xbot.shared.data.sources.remote.models.account.ReleaseWithCollectionType
import com.xbot.shared.data.sources.remote.models.account.RemoveFromCollectionRequest
import com.xbot.shared.data.sources.remote.models.account.RemoveFromFavoriteRequest
import com.xbot.shared.data.sources.remote.models.shared.GenreApi
import com.xbot.shared.data.sources.remote.models.shared.ReleasesWithMetadataApi
import com.xbot.shared.data.sources.remote.models.shared.enums.AgeRatingApi
import com.xbot.shared.data.sources.remote.models.shared.enums.ReleaseTypeApi
import com.xbot.shared.data.sources.remote.models.shared.enums.SortingTypeApi
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend fun AnilibriaClient.getCollectionAgeRatings() = request<List<AgeRatingApi>> {
    get("accounts/users/me/collections/references/age-ratings")
}

suspend fun AnilibriaClient.getCollectionGenres() = request<List<GenreApi>> {
    get("accounts/users/me/collections/references/genres")
}

suspend fun AnilibriaClient.getCollectionReleaseTypes() = request<List<ReleaseTypeApi>> {
    get("accounts/users/me/collections/references/types")
}

suspend fun AnilibriaClient.getCollectionYears() = request<List<Int>> {
    get("accounts/users/me/collections/references/years")
}

suspend fun AnilibriaClient.getCollectionReleaseIds() = request<List<ReleaseWithCollectionType>> {
    get("accounts/users/me/collections/ids")
}

suspend fun AnilibriaClient.getCollectionReleases(
    collectionType: String,
    page: Int,
    limit: Int,
    genres: List<Int>? = null,
    types: List<ReleaseTypeApi>? = null,
    fromYear: Int? = null,
    search: String? = null,
    ageRatings: List<AgeRatingApi>? = null
) = request<ReleasesWithMetadataApi> {
    get("accounts/users/me/collections/releases") {
        //TODO:
        parameter("type_of_collection", collectionType)
        parameter("page", page)
        parameter("limit", limit)
        parameter("f[genres]", genres?.joinToString(","))
        parameter("f[types]", types?.joinToString(","))
        parameter("f[years]", fromYear)
        parameter("f[search]", search)
        parameter("f[age_ratings]", ageRatings?.joinToString(","))
    }
}

//TODO:
suspend fun AnilibriaClient.addToCollection(request: List<AddToCollectionRequest>) = request<Unit> {
    post("accounts/users/me/collections") {
        setBody(request)
    }
}

//TODO:
suspend fun AnilibriaClient.removeFromCollection(request: List<RemoveFromCollectionRequest>) = request<Unit> {
    delete("accounts/users/me/collections") {
        setBody(request)
    }
}

suspend fun AnilibriaClient.getFavoriteAgeRatings() = request<List<AgeRatingApi>> {
    get("accounts/users/me/favorites/references/age-ratings")
}

suspend fun AnilibriaClient.getFavoriteGenres() = request<List<GenreApi>> {
    get("accounts/users/me/favorites/references/genres")
}

suspend fun AnilibriaClient.getFavoriteSortingTypes() = request<List<SortingTypeApi>> {
    get("accounts/users/me/favorites/references/sorting")
}

suspend fun AnilibriaClient.getFavoriteReleaseTypes() = request<List<ReleaseTypeApi>> {
    get("accounts/users/me/favorites/references/types")
}

suspend fun AnilibriaClient.getFavoriteYears() = request<List<Int>> {
    get("accounts/users/me/favorites/references/years")
}

suspend fun AnilibriaClient.getFavoriteReleaseIds() = request<List<Int>> {
    get("/accounts/users/me/favorites/ids")
}

suspend fun AnilibriaClient.getFavoriteReleases(
    page: Int,
    limit: Int,
    fromYear: Int? = null,
    types: List<ReleaseTypeApi>? = null,
    genres: List<Int>? = null,
    search: String? = null,
    sorting: SortingTypeApi? = null,
    ageRatings: List<AgeRatingApi>? = null
) = request<ReleasesWithMetadataApi> {
    get("accounts/users/me/favorites/releases") {
        parameter("page", page)
        parameter("limit", limit)
        parameter("f[years]", fromYear)
        parameter("f[types]", types?.joinToString(","))
        parameter("f[genres]", genres?.joinToString(","))
        parameter("f[search]", search)
        parameter("f[sorting]", sorting)
        parameter("f[age_ratings]", ageRatings?.joinToString(","))
    }
}

//TODO:
suspend fun AnilibriaClient.addToFavorite(request: List<AddToFavoriteRequest>) = request<Unit> {
    post("accounts/users/me/favorites") {
        setBody(request)
    }
}

//TODO:
suspend fun AnilibriaClient.removeFromFavorite(request: List<RemoveFromFavoriteRequest>) = request<Unit> {
    delete("accounts/users/me/favorites") {
        setBody(request)
    }
}

suspend fun AnilibriaClient.getUserProfile() = request<ProfileApi> {
    get("accounts/users/me/profile")
}

suspend fun AnilibriaClient.getViewedTimeCodes() = request<List<EpisodeWithTimeCode>> {
    get("accounts/users/me/views/timecodes")
}

//TODO:
suspend fun AnilibriaClient.addToViewedTimeCodes(request: List<AddTimeCodeRequest>) = request<Unit> {
    post("accounts/users/me/views/timecodes") {
        setBody(request)
    }
}

//TODO:
suspend fun AnilibriaClient.deleteViewedTimeCodes(request: List<DeleteTimeCodeRequest>) = request<Unit> {
    delete("accounts/users/me/views/timecodes") {
        setBody(request)
    }
}