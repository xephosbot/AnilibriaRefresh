package com.xbot.preference.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.common.navigation.ExternalLinkNavKey
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Settings
import com.xbot.designsystem.icons.Star
import com.xbot.resources.Res
import com.xbot.resources.preference_appearance_description
import com.xbot.resources.preference_appearance_title
import com.xbot.resources.preference_discord_description
import com.xbot.resources.preference_discord_title
import com.xbot.resources.preference_donate_description
import com.xbot.resources.preference_donate_title
import com.xbot.resources.preference_github_description
import com.xbot.resources.preference_github_title
import com.xbot.resources.preference_history_description
import com.xbot.resources.preference_history_title
import com.xbot.resources.preference_language_description
import com.xbot.resources.preference_language_title
import com.xbot.resources.preference_team_description
import com.xbot.resources.preference_team_title
import com.xbot.resources.preference_youtube_description
import com.xbot.resources.preference_youtube_title
import com.xbot.resources.tab_preference
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object PreferenceRoute : TopLevelNavKey {
    override val textRes: StringResource
        get() = Res.string.tab_preference
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Filled.Settings
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Outlined.Settings
}

sealed interface PreferenceOptionRoute : NavKey {
    val title: StringResource
    val description: StringResource
    val icon: ImageVector
}

@Serializable
data object PreferenceHistoryRoute : PreferenceOptionRoute {
    override val requiresLogin: Boolean
        get() = true
    override val title: StringResource
        get() = Res.string.preference_history_title
    override val description: StringResource
        get() = Res.string.preference_history_description
    override val icon: ImageVector
        get() = AnilibriaIcons.Filled.Star
}

@Serializable
data object PreferenceTeamRoute : PreferenceOptionRoute {
    override val title: StringResource
        get() = Res.string.preference_team_title
    override val description: StringResource
        get() = Res.string.preference_team_description
    override val icon: ImageVector = AnilibriaIcons.Filled.Star
}

@Serializable
data object PreferenceDonateRoute : PreferenceOptionRoute {
    override val title: StringResource
        get() = Res.string.preference_donate_title
    override val description: StringResource
        get() = Res.string.preference_donate_description
    override val icon: ImageVector = AnilibriaIcons.Filled.Star
}

@Serializable
data object PreferenceAppearanceRoute : PreferenceOptionRoute {
    override val title: StringResource
        get() = Res.string.preference_appearance_title
    override val description: StringResource
        get() = Res.string.preference_appearance_description
    override val icon: ImageVector = AnilibriaIcons.Filled.Settings
}

@Serializable
data object PreferenceLanguageRoute : PreferenceOptionRoute {
    override val title: StringResource
        get() = Res.string.preference_language_title
    override val description: StringResource
        get() = Res.string.preference_language_description
    override val icon: ImageVector = AnilibriaIcons.Filled.Settings
}

@Serializable
data object GitHubRoute : PreferenceOptionRoute, ExternalLinkNavKey {
    override val title: StringResource
        get() = Res.string.preference_github_title
    override val description: StringResource
        get() = Res.string.preference_github_description
    override val icon: ImageVector = AnilibriaIcons.Filled.Star
    override val url: String = "https://github.com/xephosbot/AnilibriaRefresh"
}

@Serializable
data object YouTubeRoute : PreferenceOptionRoute, ExternalLinkNavKey {
    override val title: StringResource
        get() = Res.string.preference_youtube_title
    override val description: StringResource
        get() = Res.string.preference_youtube_description
    override val icon: ImageVector = AnilibriaIcons.Filled.Star
    override val url: String = "https://youtube.com"
}

@Serializable
data object DiscordRoute : PreferenceOptionRoute, ExternalLinkNavKey {
    override val title: StringResource
        get() = Res.string.preference_discord_title
    override val description: StringResource
        get() = Res.string.preference_discord_description
    override val icon: ImageVector = AnilibriaIcons.Filled.Star
    override val url: String = "https://discord.gg"
}
