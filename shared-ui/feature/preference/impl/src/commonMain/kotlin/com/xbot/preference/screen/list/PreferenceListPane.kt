package com.xbot.preference.screen.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.PreferenceSectionHeader
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.ChevronRight
import com.xbot.designsystem.icons.OpenInNew
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.navigation.ExternalUriNavKey
import com.xbot.preference.navigation.DiscordRoute
import com.xbot.preference.navigation.GitHubRoute
import com.xbot.preference.navigation.PreferenceAppearanceRoute
import com.xbot.preference.navigation.PreferenceDonateRoute
import com.xbot.preference.navigation.PreferenceHistoryRoute
import com.xbot.preference.navigation.PreferenceLanguageRoute
import com.xbot.preference.navigation.PreferenceOptionRoute
import com.xbot.preference.navigation.PreferenceTeamRoute
import com.xbot.preference.navigation.TelegramRoute
import com.xbot.preference.navigation.YouTubeRoute
import com.xbot.resources.Res
import com.xbot.resources.preference_screen_title
import com.xbot.resources.preference_section_links
import com.xbot.resources.preference_section_main
import io.kotzilla.sdk.compose.TrackScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@TrackScreen
@Composable
internal fun PreferenceListPane(
    selectedRoute: PreferenceOptionRoute?,
    modifier: Modifier = Modifier,
    onPreferenceClick: (PreferenceOptionRoute) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(text = stringResource(Res.string.preference_screen_title))
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        PreferenceList(
            sections = PreferenceSections,
            selectedRoute = selectedRoute,
            onPreferenceClick = onPreferenceClick,
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun PreferenceList(
    sections: List<PreferenceSection>,
    selectedRoute: PreferenceOptionRoute?,
    onPreferenceClick: (PreferenceOptionRoute) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        sections.forEachIndexed { sectionIndex, section ->
            item(
                key = section.title.key,
                contentType = PreferenceListContentType.Header,
            ) {
                PreferenceSectionHeader(
                    modifier = Modifier.padding(top = if (sectionIndex > 0) SectionSpacing else 0.dp),
                    title = { Text(text = stringResource(section.title)) },
                )
            }
            itemsIndexed(
                items = section.routes,
                key = { _, route -> route.title.key },
                contentType = { _, _ -> PreferenceListContentType.Item },
            ) { index, route ->
                PreferenceItem(
                    modifier = Modifier.section(index, section.routes.size),
                    headlineContent = { Text(text = stringResource(route.title)) },
                    supportingContent = { Text(text = stringResource(route.description)) },
                    leadingContent = {
                        Icon(
                            imageVector = route.icon,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = if (route is ExternalUriNavKey) {
                                AnilibertyIcons.OpenInNew
                            } else {
                                AnilibertyIcons.ChevronRight
                            },
                            contentDescription = null
                        )
                    },
                    selected = route == selectedRoute,
                    onClick = { onPreferenceClick(route) }
                )
            }
        }
        item(contentType = PreferenceListContentType.Spacer) {
            Spacer(modifier = Modifier.height(SectionSpacing))
        }
    }
}

@Immutable
private data class PreferenceSection(
    val title: StringResource,
    val routes: List<PreferenceOptionRoute>,
)

private enum class PreferenceListContentType { Header, Item, Spacer }

private val SectionSpacing = 8.dp

private val PreferenceSections: List<PreferenceSection> = listOf(
    PreferenceSection(
        title = Res.string.preference_section_main,
        routes = listOf(
            PreferenceHistoryRoute,
            PreferenceTeamRoute,
            PreferenceDonateRoute,
            PreferenceAppearanceRoute,
            PreferenceLanguageRoute,
        ),
    ),
    PreferenceSection(
        title = Res.string.preference_section_links,
        routes = listOf(
            TelegramRoute,
            DiscordRoute,
            YouTubeRoute,
            GitHubRoute,
        ),
    ),
)

@AnilibertyPreview
@Composable
private fun PreferenceListPanePreview() {
    PreferenceListPane(
        selectedRoute = PreferenceAppearanceRoute,
        onPreferenceClick = {}
    )
}
