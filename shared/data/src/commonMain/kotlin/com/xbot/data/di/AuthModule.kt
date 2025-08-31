package com.xbot.data.di

import com.xbot.data.repository.DefaultTokenStorage
import com.xbot.data.repository.TokenStorage
import com.xbot.network.client.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::DefaultTokenStorage) { bind<TokenStorage>() }
    single<HttpClient> {
        createHttpClient {
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = get<TokenStorage>().getToken()
                        token?.let {
                            BearerTokens(accessToken = it, refreshToken = null)
                        }
                    }
                }
            }
        }
    }
}