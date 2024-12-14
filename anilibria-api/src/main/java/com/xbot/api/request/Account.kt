package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.account.ForgetPasswordRequest
import com.xbot.api.models.account.LoginRequest
import com.xbot.api.models.account.LoginResponse
import com.xbot.api.models.account.LoginSocialNetworkResponse
import com.xbot.api.models.account.ResetPasswordRequest
import com.xbot.api.models.shared.enums.SocialNetworkEnum
import com.xbot.api.models.account.AddToCollectionRequest
import com.xbot.api.models.account.ReleaseWithCollectionType
import com.xbot.api.models.account.RemoveFromCollectionRequest
import com.xbot.api.models.account.AddToFavoriteRequest
import com.xbot.api.models.account.RemoveFromFavoriteRequest
import com.xbot.api.models.account.Profile
import com.xbot.api.models.account.AddTimeCodeRequest
import com.xbot.api.models.account.DeleteTimeCodeRequest
import com.xbot.api.models.account.EpisodeWithTimeCode
import com.xbot.api.models.shared.Genre
import com.xbot.api.models.shared.LabeledValue
import com.xbot.api.models.shared.ReleasesWithMetadata
import com.xbot.api.models.shared.enums.AgeRatingEnum
import com.xbot.api.models.shared.enums.ReleaseTypeEnum
import com.xbot.api.models.shared.enums.SortingTypeEnum
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend fun AnilibriaClient.login(login: String, password: String): LoginResponse = request {
    post("accounts/users/auth/login") {
        setBody(LoginRequest(login, password))
    }
}

suspend fun AnilibriaClient.logout(): LoginResponse = request {
    post("accounts/users/auth/logout")
}

suspend fun AnilibriaClient.loginWithSocialNetwork(provider: SocialNetworkEnum): LoginSocialNetworkResponse = request {
    get("accounts/users/auth/social/${provider}/login")
}

suspend fun AnilibriaClient.authenticateWithSocialNetwork(state: String): LoginResponse = request {
    get("accounts/users/auth/social/authenticate") {
        parameter("state", state)
    }
}

suspend fun AnilibriaClient.forgetPassword(email: String): Unit = request {
    get("accounts/users/auth/password/forget") {
        setBody(ForgetPasswordRequest(email))
    }
}

suspend fun AnilibriaClient.resetPassword(token: String, password: String, passwordConfirmation: String): Unit = request {
    post("accounts/users/auth/password/reset") {
        setBody(ResetPasswordRequest(token, password, passwordConfirmation))
    }
}

suspend fun AnilibriaClient.getCollectionAgeRatings() = request<List<LabeledValue<AgeRatingEnum>>> {
    get("accounts/users/me/collections/references/age-ratings")
}.map(LabeledValue<AgeRatingEnum>::value)

suspend fun AnilibriaClient.getCollectionGenres(): List<Genre> = request {
    get("accounts/users/me/collections/references/genres")
}

suspend fun AnilibriaClient.getCollectionReleaseTypes() = request<List<LabeledValue<ReleaseTypeEnum>>> {
    get("accounts/users/me/collections/references/types")
}.map(LabeledValue<ReleaseTypeEnum>::value)

suspend fun AnilibriaClient.getCollectionYears(): List<Int> = request {
    get("accounts/users/me/collections/references/years")
}

suspend fun AnilibriaClient.getCollectionReleaseIds(): List<ReleaseWithCollectionType> = request {
    get("accounts/users/me/collections/ids")
}

suspend fun AnilibriaClient.getCollectionReleases(
    collectionType: String,
    page: Int,
    limit: Int,
    genres: List<Int>? = null,
    types: List<ReleaseTypeEnum>? = null,
    fromYear: Int? = null,
    search: String? = null,
    ageRatings: List<AgeRatingEnum>? = null
): ReleasesWithMetadata = request {
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
suspend fun AnilibriaClient.addToCollection(request: List<AddToCollectionRequest>): Unit = request {
    post("accounts/users/me/collections") {
        setBody(request)
    }
}

//TODO:
suspend fun AnilibriaClient.removeFromCollection(request: List<RemoveFromCollectionRequest>): Unit = request {
    delete("accounts/users/me/collections") {
        setBody(request)
    }
}

suspend fun AnilibriaClient.getFavoriteAgeRatings() = request<List<LabeledValue<AgeRatingEnum>>> {
    get("accounts/users/me/favorites/references/age-ratings")
}.map(LabeledValue<AgeRatingEnum>::value)

suspend fun AnilibriaClient.getFavoriteGenres(): List<Genre> = request {
    get("accounts/users/me/favorites/references/genres")
}

suspend fun AnilibriaClient.getFavoriteSortingTypes() = request<List<LabeledValue<SortingTypeEnum>>> {
    get("accounts/users/me/favorites/references/sorting")
}.map(LabeledValue<SortingTypeEnum>::value)

suspend fun AnilibriaClient.getFavoriteReleaseTypes() = request<List<LabeledValue<ReleaseTypeEnum>>> {
    get("accounts/users/me/favorites/references/types")
}.map(LabeledValue<ReleaseTypeEnum>::value)

suspend fun AnilibriaClient.getFavoriteYears(): List<Int> = request {
    get("accounts/users/me/favorites/references/years")
}

suspend fun AnilibriaClient.getFavoriteReleaseIds(): List<Int> = request {
    get("/accounts/users/me/favorites/ids")
}

suspend fun AnilibriaClient.getFavoriteReleases(
    page: Int,
    limit: Int,
    fromYear: Int? = null,
    types: List<ReleaseTypeEnum>? = null,
    genres: List<Int>? = null,
    search: String? = null,
    sorting: com.xbot.api.models.shared.enums.SortingTypeEnum? = null,
    ageRatings: List<AgeRatingEnum>? = null
): ReleasesWithMetadata = request {
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
suspend fun AnilibriaClient.addToFavorite(request: List<AddToFavoriteRequest>): Unit = request {
    post("accounts/users/me/favorites") {
        setBody(request)
    }
}

//TODO:
suspend fun AnilibriaClient.removeFromFavorite(request: List<RemoveFromFavoriteRequest>): Unit = request {
    delete("accounts/users/me/favorites") {
        setBody(request)
    }
}

suspend fun AnilibriaClient.getUserProfile(): Profile = request {
    get("accounts/users/me/profile")
}

suspend fun AnilibriaClient.getViewedTimeCodes(): List<EpisodeWithTimeCode> = request {
    get("accounts/users/me/views/timecodes")
}

//TODO:
suspend fun AnilibriaClient.addToViewedTimeCodes(request: List<AddTimeCodeRequest>): Unit = request {
    post("accounts/users/me/views/timecodes") {
        setBody(request)
    }
}

//TODO:
suspend fun AnilibriaClient.deleteViewedTimeCodes(request: List<DeleteTimeCodeRequest>): Unit = request {
    delete("accounts/users/me/views/timecodes") {
        setBody(request)
    }
}