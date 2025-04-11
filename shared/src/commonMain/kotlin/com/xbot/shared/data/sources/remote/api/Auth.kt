package com.xbot.shared.data.sources.remote.api

import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.models.account.AddTimeCodeRequest
import com.xbot.shared.data.sources.remote.models.account.AddToCollectionRequest
import com.xbot.shared.data.sources.remote.models.account.AddToFavoriteRequest
import com.xbot.shared.data.sources.remote.models.account.DeleteTimeCodeRequest
import com.xbot.shared.data.sources.remote.models.account.EpisodeWithTimeCode
import com.xbot.shared.data.sources.remote.models.account.ForgetPasswordRequest
import com.xbot.shared.data.sources.remote.models.account.LoginRequest
import com.xbot.shared.data.sources.remote.models.account.LoginResponse
import com.xbot.shared.data.sources.remote.models.account.LoginSocialNetworkResponse
import com.xbot.shared.data.sources.remote.models.account.ProfileApi
import com.xbot.shared.data.sources.remote.models.account.ReleaseWithCollectionType
import com.xbot.shared.data.sources.remote.models.account.ResetPasswordRequest
import com.xbot.shared.data.sources.remote.models.shared.enums.SocialNetwork
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
