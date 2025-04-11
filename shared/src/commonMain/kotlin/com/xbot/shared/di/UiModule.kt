package com.xbot.shared.di

import com.xbot.shared.ui.designsystem.utils.SnackbarManager
import org.koin.dsl.module

val uiModule = module {
    factory { SnackbarManager }
}
