package com.xbot.anilibriarefresh.di

import com.xbot.designsystem.utils.SnackbarManager
import org.koin.dsl.module

val uiModule = module {
    factory { SnackbarManager }
}
