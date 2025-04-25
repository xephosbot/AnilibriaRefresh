package com.xbot.shared.di

import com.xbot.shared.data.repositories.DefaultCatalogRepository
import com.xbot.shared.data.repositories.DefaultReleaseRepository
import com.xbot.shared.data.repositories.DefaultUserRepository
import com.xbot.shared.data.sources.local.TokenDataStore
import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.TokenProvider
import com.xbot.shared.domain.repository.CatalogRepository
import com.xbot.shared.domain.repository.ReleaseRepository
import com.xbot.shared.domain.repository.UserRepository
import com.xbot.shared.domain.usecase.GetCatalogFilters
import com.xbot.shared.domain.usecase.GetReleasesFeed
import com.xbot.shared.domain.usecase.GetReleasesPager
import com.xbot.shared.ui.designsystem.utils.SnackbarManager
import com.xbot.shared.ui.feature.home.HomeViewModel
import com.xbot.shared.ui.feature.home.ScheduleViewModel
import com.xbot.shared.ui.feature.profile.ProfileViewModel
import com.xbot.shared.ui.feature.search.SearchViewModel
import com.xbot.shared.ui.feature.title.TitleViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    singleOf(::TokenDataStore) { bind<TokenProvider>() }
    singleOf(::AnilibriaClient)

    singleOf(::DefaultReleaseRepository) { bind<ReleaseRepository>() }
    singleOf(::DefaultCatalogRepository) { bind<CatalogRepository>() }
    singleOf(::DefaultUserRepository) { bind<UserRepository>() }

    factoryOf(::GetReleasesFeed)
    factoryOf(::GetCatalogFilters)
    factoryOf(::GetReleasesPager)

    factory { SnackbarManager }

    viewModelOf(::HomeViewModel)
    viewModelOf(::ScheduleViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::TitleViewModel)
    //viewModelOf(::PlayerViewModel)
    viewModelOf(::ProfileViewModel)
}