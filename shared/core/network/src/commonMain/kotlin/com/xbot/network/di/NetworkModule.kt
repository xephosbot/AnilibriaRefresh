package com.xbot.network.di

import co.touchlab.kermit.Logger as KermitLogger
import com.xbot.logger.AppLogger
import com.xbot.network.Constants
import com.xbot.network.api.AdsApi
import com.xbot.network.api.AuthApi
import com.xbot.network.api.CatalogApi
import com.xbot.network.api.CollectionApi
import com.xbot.network.api.EpisodesApi
import com.xbot.network.api.FavoritesApi
import com.xbot.network.api.FranchisesApi
import com.xbot.network.api.GenresApi
import com.xbot.network.api.OtpApi
import com.xbot.network.api.ProfileApi
import com.xbot.network.api.PromotionsApi
import com.xbot.network.api.ReleasesApi
import com.xbot.network.api.ScheduleApi
import com.xbot.network.api.SearchApi
import com.xbot.network.api.TeamsApi
import com.xbot.network.api.TorrentsApi
import com.xbot.network.api.VideosApi
import com.xbot.network.api.ViewsApi
import com.xbot.network.api.createAdsApi
import com.xbot.network.api.createAuthApi
import com.xbot.network.api.createCatalogApi
import com.xbot.network.api.createCollectionApi
import com.xbot.network.api.createEpisodesApi
import com.xbot.network.api.createFavoritesApi
import com.xbot.network.api.createFranchisesApi
import com.xbot.network.api.createGenresApi
import com.xbot.network.api.createOtpApi
import com.xbot.network.api.createProfileApi
import com.xbot.network.api.createPromotionsApi
import com.xbot.network.api.createReleasesApi
import com.xbot.network.api.createScheduleApi
import com.xbot.network.api.createSearchApi
import com.xbot.network.api.createTeamsApi
import com.xbot.network.api.createTorrentsApi
import com.xbot.network.api.createVideosApi
import com.xbot.network.api.createViewsApi
import com.xbot.network.client.EitherConverterFactory
import com.xbot.network.client.NetworkRetry
import com.xbot.network.client.SessionStorage
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@Configuration
@ComponentScan("com.xbot.network")
class NetworkModule {

    @Singleton
    internal fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Singleton
    internal fun createHttpClient(sessionStorage: Lazy<SessionStorage>, json: Json): HttpClient =
        HttpClient {
            expectSuccess = true

            defaultRequest {
                url(Constants.BASE_URL_API)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(json)
            }

            NetworkRetry()

            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 10_000
            }

            Auth {
                bearer {
                    cacheTokens = false
                    loadTokens {
                        sessionStorage.value.getToken()
                    }
                    refreshTokens {
                        sessionStorage.value.clearToken()
                        null
                    }
                    sendWithoutRequest { request -> request.requiresAuth() }
                }
            }

            Logging {
                this.logger = object : KtorLogger {
                    private val log = KermitLogger.withTag("Ktor")
                    override fun log(message: String) {
                        log.i { message }
                    }
                }
                this.level = LogLevel.INFO
            }

            ContentEncoding {
                gzip()
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    val clientException =
                        exception as? ClientRequestException
                            ?: return@handleResponseExceptionWithRequest
                    val exceptionResponse = clientException.response

                    if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                        sessionStorage.value.clearToken()
                    }
                }
            }
        }

    @Singleton
    internal fun provideKtorfit(client: HttpClient, logger: Lazy<AppLogger>): Ktorfit =
        Ktorfit.Builder()
            .baseUrl(Constants.BASE_URL_API)
            .httpClient(client)
            .converterFactories(EitherConverterFactory(logger))
            .build()

    @Singleton
    internal fun provideAdsApi(ktorfit: Ktorfit): AdsApi = ktorfit.createAdsApi()

    @Singleton
    internal fun provideAuthApi(ktorfit: Ktorfit): AuthApi = ktorfit.createAuthApi()

    @Singleton
    internal fun provideCatalogApi(ktorfit: Ktorfit): CatalogApi = ktorfit.createCatalogApi()

    @Singleton
    internal fun provideCollectionApi(ktorfit: Ktorfit): CollectionApi =
        ktorfit.createCollectionApi()

    @Singleton
    internal fun provideEpisodesApi(ktorfit: Ktorfit): EpisodesApi = ktorfit.createEpisodesApi()

    @Singleton
    internal fun provideFavoritesApi(ktorfit: Ktorfit): FavoritesApi = ktorfit.createFavoritesApi()

    @Singleton
    internal fun provideFranchisesApi(ktorfit: Ktorfit): FranchisesApi =
        ktorfit.createFranchisesApi()

    @Singleton
    internal fun provideGenresApi(ktorfit: Ktorfit): GenresApi = ktorfit.createGenresApi()

    @Singleton
    internal fun provideOtpApi(ktorfit: Ktorfit): OtpApi = ktorfit.createOtpApi()

    @Singleton
    internal fun provideProfileApi(ktorfit: Ktorfit): ProfileApi = ktorfit.createProfileApi()

    @Singleton
    internal fun providePromotionsApi(ktorfit: Ktorfit): PromotionsApi =
        ktorfit.createPromotionsApi()

    @Singleton
    internal fun provideReleasesApi(ktorfit: Ktorfit): ReleasesApi = ktorfit.createReleasesApi()

    @Singleton
    internal fun provideScheduleApi(ktorfit: Ktorfit): ScheduleApi = ktorfit.createScheduleApi()

    @Singleton
    internal fun provideSearchApi(ktorfit: Ktorfit): SearchApi = ktorfit.createSearchApi()

    @Singleton
    internal fun provideTeamsApi(ktorfit: Ktorfit): TeamsApi = ktorfit.createTeamsApi()

    @Singleton
    internal fun provideTorrentsApi(ktorfit: Ktorfit): TorrentsApi = ktorfit.createTorrentsApi()

    @Singleton
    internal fun provideVideosApi(ktorfit: Ktorfit): VideosApi = ktorfit.createVideosApi()

    @Singleton
    internal fun provideViewsApi(ktorfit: Ktorfit): ViewsApi = ktorfit.createViewsApi()
}

// Endpoints scoped to the signed-in user; every other request goes out without a token.
private fun HttpRequestBuilder.requiresAuth(): Boolean {
    val path = url.encodedPath
    return "/accounts/users/me/" in path || path.endsWith("/accounts/users/auth/logout")
}
