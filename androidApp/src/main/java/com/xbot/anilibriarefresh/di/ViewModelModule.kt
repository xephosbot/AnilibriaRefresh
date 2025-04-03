/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.anilibriarefresh.di

import com.xbot.title.TitleViewModel
import com.xbot.home.HomeViewModel
import com.xbot.home.ScheduleViewModel
import com.xbot.search.SearchViewModel
import com.xbot.player.PlayerViewModel
import com.xbot.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ScheduleViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::TitleViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::ProfileViewModel)
}
