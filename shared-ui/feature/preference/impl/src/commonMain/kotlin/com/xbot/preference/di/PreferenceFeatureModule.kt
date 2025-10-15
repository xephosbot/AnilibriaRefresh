package com.xbot.preference.di

import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.preference.history.HistoryViewModel
import com.xbot.preference.PreferenceScreen
import com.xbot.preference.ProfileViewModel
import com.xbot.preference.navigation.PreferenceRoute
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val preferenceFeatureModule = module {
    single<NavEntryBuilder>(named("feature/preference")) {
        { navigator ->
            entry<PreferenceRoute> {
                PreferenceScreen(
                    onReleaseClick = {

                    },
                )
            }
        }
    }
    viewModelOf(::ProfileViewModel)
    viewModelOf(::HistoryViewModel)
}