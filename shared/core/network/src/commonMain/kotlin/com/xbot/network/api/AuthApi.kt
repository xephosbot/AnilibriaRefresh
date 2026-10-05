package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.enums.SocialTypeDto
import com.xbot.network.models.requests.ForgotPasswordRequest
import com.xbot.network.models.requests.LoginRequest
import com.xbot.network.models.requests.ResetPasswordRequest
import com.xbot.network.models.responses.AuthResponse
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.LogoutResponse
import com.xbot.network.models.responses.SocialAuthResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface AuthApi {
    @POST("accounts/users/auth/login")
    suspend fun login(@Body request: LoginRequest): Either<AppError, LoginResponse>

    @POST("accounts/users/auth/logout")
    suspend fun logout(): Either<AppError, LogoutResponse>

    @GET("accounts/users/auth/social/{provider}/login")
    suspend fun socialLogin(
        @Path("provider") provider: SocialTypeDto
    ): Either<AppError, SocialAuthResponse>

    @GET("accounts/users/auth/social/authenticate")
    suspend fun socialAuthenticate(@Query("state") state: String): Either<AppError, AuthResponse>

    @GET("accounts/users/auth/password/forget")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Either<AppError, Unit>

    @POST("accounts/users/auth/password/reset")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Either<AppError, Unit>
}
