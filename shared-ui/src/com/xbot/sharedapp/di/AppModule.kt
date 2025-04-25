package com.xbot.sharedapp.di

import com.xbot.api.di.dataStoreModule
import com.xbot.api.di.networkModule
import com.xbot.data.di.repositoryModule
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.di.useCaseModule
import com.xbot.home.HomeViewModel
import com.xbot.home.ScheduleViewModel
import com.xbot.profile.ProfileViewModel
import com.xbot.search.SearchViewModel
import com.xbot.title.TitleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    includes(networkModule, dataStoreModule, repositoryModule, useCaseModule)

    factory { SnackbarManager }

    viewModelOf(::HomeViewModel)
    viewModelOf(::ScheduleViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::TitleViewModel)
    viewModelOf(::ProfileViewModel)
}