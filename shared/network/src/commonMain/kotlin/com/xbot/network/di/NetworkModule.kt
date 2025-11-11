package com.xbot.network.di

import com.xbot.network.api.AdsApi
import com.xbot.network.api.AuthApi
import com.xbot.network.api.CatalogApi
import com.xbot.network.api.CollectionApi
import com.xbot.network.api.DefaultAdsApi
import com.xbot.network.api.DefaultAuthApi
import com.xbot.network.api.DefaultCatalogApi
import com.xbot.network.api.DefaultCollectionApi
import com.xbot.network.api.DefaultEpisodesApi
import com.xbot.network.api.DefaultFavoritesApi
import com.xbot.network.api.DefaultFranchisesApi
import com.xbot.network.api.DefaultGenresApi
import com.xbot.network.api.DefaultOtpApi
import com.xbot.network.api.DefaultProfileApi
import com.xbot.network.api.DefaultPromotionsApi
import com.xbot.network.api.DefaultReleasesApi
import com.xbot.network.api.DefaultScheduleApi
import com.xbot.network.api.DefaultSearchApi
import com.xbot.network.api.DefaultTeamsApi
import com.xbot.network.api.DefaultTorrentsApi
import com.xbot.network.api.DefaultVideosApi
import com.xbot.network.api.DefaultViewsApi
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
import com.xbot.network.client.createHttpClient
import io.ktor.client.HttpClient
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(KoinInternalApi::class)
val networkModule = module {
    single<HttpClient> {
        createHttpClient(get(), getKoin().logger)
    }
    singleOf(::DefaultAdsApi) { bind<AdsApi>() }
    singleOf(::DefaultAuthApi) { bind<AuthApi>() }
    singleOf(::DefaultCatalogApi) { bind<CatalogApi>() }
    singleOf(::DefaultCollectionApi) { bind<CollectionApi>() }
    singleOf(::DefaultEpisodesApi) { bind<EpisodesApi>() }
    singleOf(::DefaultFavoritesApi) { bind<FavoritesApi>() }
    singleOf(::DefaultFranchisesApi) { bind<FranchisesApi>() }
    singleOf(::DefaultGenresApi) { bind<GenresApi>() }
    singleOf(::DefaultOtpApi) { bind<OtpApi>() }
    singleOf(::DefaultProfileApi) { bind<ProfileApi>() }
    singleOf(::DefaultPromotionsApi) { bind<PromotionsApi>() }
    singleOf(::DefaultReleasesApi) { bind<ReleasesApi>() }
    singleOf(::DefaultScheduleApi) { bind<ScheduleApi>() }
    singleOf(::DefaultSearchApi) { bind<SearchApi>() }
    singleOf(::DefaultTeamsApi) { bind<TeamsApi>() }
    singleOf(::DefaultTorrentsApi) { bind<TorrentsApi>() }
    singleOf(::DefaultVideosApi) { bind<VideosApi>() }
    singleOf(::DefaultViewsApi) { bind<ViewsApi>() }
}