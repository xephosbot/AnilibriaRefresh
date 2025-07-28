package com.xbot.domain.models

sealed class AppTheme {
    data object Light : AppTheme()
    data object Dark : AppTheme()
    data object System : AppTheme()
}
