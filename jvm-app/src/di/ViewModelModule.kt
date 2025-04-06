package com.xbot.anilibriarefresh.di

import com.xbot.home.HomeViewModel
import com.xbot.home.ScheduleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ScheduleViewModel)
}