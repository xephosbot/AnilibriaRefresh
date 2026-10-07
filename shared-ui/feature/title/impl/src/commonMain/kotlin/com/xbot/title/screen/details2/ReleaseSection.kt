package com.xbot.title.screen.details2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.xbot.resources.Res
import com.xbot.resources.release_details_tab_about
import com.xbot.resources.release_details_tab_episodes
import com.xbot.resources.release_details_tab_ratings
import com.xbot.title.component.PaneSection
import com.xbot.title.component.PaneSectionRole
import org.jetbrains.compose.resources.stringResource

internal enum class ReleaseSection { Header, Episodes, About, Ratings }

internal fun ReleaseTab.toSection(): ReleaseSection = when (this) {
    ReleaseTab.Episodes -> ReleaseSection.Episodes
    ReleaseTab.About -> ReleaseSection.About
    ReleaseTab.Ratings -> ReleaseSection.Ratings
}

internal fun ReleaseSection.toTab(): ReleaseTab? = when (this) {
    ReleaseSection.Header -> null
    ReleaseSection.Episodes -> ReleaseTab.Episodes
    ReleaseSection.About -> ReleaseTab.About
    ReleaseSection.Ratings -> ReleaseTab.Ratings
}

@Composable
internal fun rememberReleaseSections(): List<PaneSection<ReleaseSection>> {
    val episodesTitle = stringResource(Res.string.release_details_tab_episodes)
    val aboutTitle = stringResource(Res.string.release_details_tab_about)
    val ratingsTitle = stringResource(Res.string.release_details_tab_ratings)
    return remember(episodesTitle, aboutTitle, ratingsTitle) {
        listOf(
            PaneSection(ReleaseSection.Header, PaneSectionRole.Header),
            PaneSection(ReleaseSection.Episodes, PaneSectionRole.Supporting, episodesTitle),
            PaneSection(ReleaseSection.About, PaneSectionRole.Main, aboutTitle),
            PaneSection(ReleaseSection.Ratings, PaneSectionRole.Extra, ratingsTitle)
        )
    }
}
