package com.xbot.title

sealed interface TitleScreenAction {
    data object Refresh : TitleScreenAction
}
