package com.xbot.shared.domain.models

sealed class AppTheme {
    data object Light : AppTheme()
    data object Dark : AppTheme()
    data object System : AppTheme()
}
