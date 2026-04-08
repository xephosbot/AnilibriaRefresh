package com.xbot.di

import com.xbot.home.HomeViewModel
import com.xbot.login.LoginViewModel
import com.xbot.player.PlayerViewModel
import com.xbot.preference.appearance.AppearanceViewModel
import com.xbot.preference.donate.DonateViewModel
import com.xbot.preference.history.HistoryViewModel
import com.xbot.preference.team.TeamViewModel
import com.xbot.search.SearchViewModel
import com.xbot.title.TitleViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

internal val viewModelModule = module {
    viewModel<HomeViewModel>()
    viewModel<LoginViewModel>()
    viewModel<PlayerViewModel>()
    viewModel<AppearanceViewModel>()
    viewModel<DonateViewModel>()
    viewModel<HistoryViewModel>()
    viewModel<TeamViewModel>()
    viewModel<SearchViewModel>()
    viewModel<TitleViewModel>()
}
