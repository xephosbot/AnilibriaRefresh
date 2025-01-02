/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.anilibriarefresh.di

import com.xbot.anilibriarefresh.ui.PlayerViewModel
import com.xbot.anilibriarefresh.ui.feature.favorite.FavoriteViewModel
import com.xbot.anilibriarefresh.ui.feature.home.feed.HomeFeedViewModel
import com.xbot.anilibriarefresh.ui.feature.home.search.FiltersViewModel
import com.xbot.anilibriarefresh.ui.feature.home.search.SearchResultViewModel
import com.xbot.anilibriarefresh.ui.feature.title.TitleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::HomeFeedViewModel)
    viewModelOf(::FiltersViewModel)
    viewModelOf(::SearchResultViewModel)
    viewModelOf(::TitleViewModel)
    viewModelOf(::PlayerViewModel)
}
