/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.anilibriarefresh.di

import com.xbot.anilibriarefresh.ui.PlayerViewModel
import com.xbot.title.TitleViewModel
import com.xbot.home.HomeViewModel
import com.xbot.search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::TitleViewModel)
    viewModelOf(::PlayerViewModel)
}
