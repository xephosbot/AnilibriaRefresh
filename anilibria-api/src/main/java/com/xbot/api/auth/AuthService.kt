package com.xbot.api.auth

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.auth.models.LoginRequest
import com.xbot.api.auth.models.LoginResponse
import com.xbot.api.auth.models.LoginSocialNetwork
import com.xbot.api.shared.enums.SocialNetworkEnum
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {
    @POST("accounts/users/auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest,
    ): ApiResponse<LoginResponse>

    @POST("accounts/users/auth/logout")
    suspend fun logoutUser(): ApiResponse<LoginResponse>

    @GET("accounts/users/auth/social/{provider}/login")
    suspend fun loginWithSocialNetwork(
        @Path("provider") provider: com.xbot.api.shared.enums.SocialNetworkEnum,
    ): ApiResponse<LoginSocialNetwork>

    @GET("accounts/users/auth/social/authenticate")
    suspend fun authenticateWithSocialNetwork(
        @Query("state") state: String,
    ): ApiResponse<LoginResponse>
}