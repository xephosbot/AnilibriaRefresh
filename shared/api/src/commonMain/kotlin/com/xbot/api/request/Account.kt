package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.account.ForgetPasswordRequest
import com.xbot.api.models.account.LoginRequest
import com.xbot.api.models.account.LoginResponse
import com.xbot.api.models.account.LoginSocialNetworkResponse
import com.xbot.api.models.account.ResetPasswordRequest
import com.xbot.api.models.shared.enums.SocialNetwork
import com.xbot.api.models.account.AddToCollectionRequest
import com.xbot.api.models.account.ReleaseWithCollectionType
import com.xbot.api.models.account.RemoveFromCollectionRequest
import com.xbot.api.models.account.AddToFavoriteRequest
import com.xbot.api.models.account.RemoveFromFavoriteRequest
import com.xbot.api.models.account.Profile
import com.xbot.api.models.account.AddTimeCodeRequest
import com.xbot.api.models.account.DeleteTimeCodeRequest
import com.xbot.api.models.account.EpisodeWithTimeCode
import com.xbot.api.models.shared.GenreApi
import com.xbot.api.models.shared.ReleasesWithMetadataApi
import com.xbot.api.models.shared.enums.AgeRatingApi
import com.xbot.api.models.shared.enums.ReleaseTypeApi
import com.xbot.api.models.shared.enums.SortingTypeApi
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend fun AnilibriaClient.login(login: String, password: String) = request<LoginResponse> {
    post("accounts/users/auth/login") {
        setBody(LoginRequest(login, password))
    }
}

suspend fun AnilibriaClient.logout() = request<LoginResponse> {
    post("accounts/users/auth/logout")
}

suspend fun AnilibriaClient.loginWithSocialNetwork(provider: SocialNetwork) = request<LoginSocialNetworkResponse> {
    get("accounts/users/auth/social/${provider}/login")
}

suspend fun AnilibriaClient.authenticateWithSocialNetwork(state: String) = request<LoginResponse> {
    get("accounts/users/auth/social/authenticate") {
        parameter("state", state)
    }
}

suspend fun AnilibriaClient.forgetPassword(email: String) = request<Unit> {
    get("accounts/users/auth/password/forget") {
        setBody(ForgetPasswordRequest(email))
    }
}

suspend fun AnilibriaClient.resetPassword(token: String, password: String, passwordConfirmation: String) = request<Unit> {
    post("accounts/users/auth/password/reset") {
        setBody(ResetPasswordRequest(token, password, passwordConfirmation))
    }
}

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
    sorting: com.xbot.api.models.shared.enums.SortingTypeApi? = null,
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

suspend fun AnilibriaClient.getUserProfile() = request<Profile> {
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