package com.xbot.common.navigation

inline fun <reified T : NavKey> Navigator.replace(key: NavKey) {
    if (currentDestination == key) return
    if (currentDestination is T) {
        navigateBack()
    }
    navigate(key)
}
