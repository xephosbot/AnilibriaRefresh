package com.xbot.shared.di

import com.xbot.shared.ui.feature.home.HomeViewModel
import com.xbot.shared.ui.feature.home.ScheduleViewModel
import com.xbot.shared.ui.feature.profile.ProfileViewModel
import com.xbot.shared.ui.feature.search.SearchViewModel
import com.xbot.shared.ui.feature.title.TitleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ScheduleViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::TitleViewModel)
    //viewModelOf(::PlayerViewModel)
    viewModelOf(::ProfileViewModel)
}