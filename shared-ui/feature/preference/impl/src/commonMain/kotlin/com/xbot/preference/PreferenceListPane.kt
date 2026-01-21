package com.xbot.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.common.navigation.ExternalLinkNavKey
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ChevronRight
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.LocalIsSinglePane
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.di.domainModule
import com.xbot.fixtures.di.fixturesModule
import com.xbot.preference.navigation.DiscordRoute
import com.xbot.preference.navigation.GitHubRoute
import com.xbot.preference.navigation.PreferenceAppearanceRoute
import com.xbot.preference.navigation.PreferenceDonateRoute
import com.xbot.preference.navigation.PreferenceHistoryRoute
import com.xbot.preference.navigation.PreferenceOptionRoute
import com.xbot.preference.navigation.PreferenceTeamRoute
import com.xbot.preference.navigation.YouTubeRoute
import com.xbot.resources.Res
import com.xbot.resources.preference_screen_title
import com.xbot.resources.preference_section_links
import com.xbot.resources.preference_section_main
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.dsl.module

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
internal fun PreferenceListPane(
    modifier: Modifier = Modifier,
    currentDestination: PreferenceOptionRoute?,
    onNavigateToDetail: (PreferenceOptionRoute) -> Unit,
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
        PreferencesList(
            preferences = PreferenceListDefaults.sections,
            currentDestination = currentDestination,
            contentPadding = innerPadding,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

@Composable
private fun PreferencesList(
    preferences: Map<StringResource, List<PreferenceOptionRoute>>,
    currentDestination: PreferenceOptionRoute?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onNavigateToDetail: (PreferenceOptionRoute) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val isSinglePane = LocalIsSinglePane.current

    ProvideShimmer(shimmer) {
        LazyColumn(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
        ) {
            preferences.forEach { (title, items) ->
                item {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = 24.dp,
                            vertical = 8.dp,
                        ),
                        text = stringResource(title),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                itemsIndexed(items) { index, item ->
                    val isSelected = item == currentDestination && !isSinglePane
                    
                    PreferenceItem(
                        modifier = Modifier.section(index, items.size),
                        headlineContent = { Text(text = stringResource(item.title)) },
                        supportingContent = { Text(text = stringResource(item.description)) },
                        leadingContent = {
                            Icon(
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                                imageVector = item.icon,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            if (item !is ExternalLinkNavKey) {
                                Icon(
                                    imageVector = AnilibriaIcons.ChevronRight,
                                    contentDescription = null
                                )
                            }
                        },
                        selected = isSelected,
                        onClick = { onNavigateToDetail(item) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

object PreferenceListDefaults {
    val sections: Map<StringResource, List<PreferenceOptionRoute>> = mapOf(
        Res.string.preference_section_main to listOf(
            PreferenceHistoryRoute,
            PreferenceTeamRoute,
            PreferenceDonateRoute,
            PreferenceAppearanceRoute
        ),
        Res.string.preference_section_links to listOf(
            GitHubRoute,
            YouTubeRoute,
            DiscordRoute
        )
    )
}

@Preview
@Composable
private fun PreferenceListPanePreview() {
    AnilibriaPreview {
        KoinApplicationPreview(
            application = {
                modules(
                    domainModule,
                    fixturesModule,
                    module {
                        single { SnackbarManager }
                    }
                )
            }
        ) {
            PreferenceListPane(
                currentDestination = null,
                onNavigateToDetail = {}
            )
        }
    }
}
