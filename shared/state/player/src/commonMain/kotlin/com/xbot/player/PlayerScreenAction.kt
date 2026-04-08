package com.xbot.player

import com.xbot.domain.models.Episode

sealed interface PlayerScreenAction {
    data class OnEpisodeChange(val episode: Episode) : PlayerScreenAction
    data class OnQualityChange(val quality: VideoQuality) : PlayerScreenAction
}
