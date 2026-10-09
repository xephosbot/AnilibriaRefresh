package com.xbot.title

import com.xbot.domain.models.enums.CollectionType

sealed interface TitleScreenAction {
    data object Refresh : TitleScreenAction
    data class OnTabSelect(val tab: ReleaseTab) : TitleScreenAction
    data class OnEpisodesSortChange(val sort: EpisodesSort) : TitleScreenAction
    data object OnFavoriteToggle : TitleScreenAction
    data class OnCollectionStatusSelect(val status: CollectionType?) : TitleScreenAction
}
