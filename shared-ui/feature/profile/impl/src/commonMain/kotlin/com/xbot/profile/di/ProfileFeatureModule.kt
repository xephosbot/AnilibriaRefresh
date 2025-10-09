package com.xbot.profile.di

import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.profile.HistoryViewModel
import com.xbot.profile.ProfileScreen
import com.xbot.profile.ProfileViewModel
import com.xbot.profile.navigation.ProfileRoute
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileFeatureModule = module {
    single<NavEntryBuilder>(named("feature/profile")) {
        { navigator ->
            entry<ProfileRoute> {
                ProfileScreen(
                    onReleaseClick = {

                    },
                )
            }
        }
    }
    viewModelOf(::ProfileViewModel)
    viewModelOf(::HistoryViewModel)
}