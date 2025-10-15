package com.xbot.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.items
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.preference.ui.ProfileItem

@Composable
internal fun PreferenceListPane(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    isExpandedLayout: Boolean,
    currentDestination: PreferenceDestination?,
    onNavigateToDetail: (PreferenceDestination) -> Unit,
    onOpenUrl: (String) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        PreferencesList(
            isExpandedLayout = isExpandedLayout,
            state = state,
            preferences = PreferenceListDefaults.allSections,
            currentDestination = currentDestination,
            contentPadding = innerPadding,
            onNavigateToDetail = onNavigateToDetail,
            onOpenUrl = onOpenUrl,
        )
    }
}

@Composable
private fun PreferencesList(
    state: ProfileScreenState,
    isExpandedLayout: Boolean,
    preferences: List<PreferenceSection>,
    currentDestination: PreferenceDestination?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onNavigateToDetail: (PreferenceDestination) -> Unit,
    onOpenUrl: (String) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        LazyColumn(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
        ) {
            preferences.forEach { section ->
                section(
                    header = section.title?.let { title ->
                        {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 24.dp,
                                    vertical = 8.dp,
                                ),
                                text = title,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    } ?: {
                        Spacer(modifier = Modifier.height(16.dp))
                    },
                    footer = {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                ) {
                    items(section.items) { item ->
                        when (item) {
                            is PreferenceItem.ExternalLink -> {
                                ListItem(
                                    modifier = Modifier
                                        .clickable { onOpenUrl(item.url) },
                                    headlineContent = { Text(text = item.title) },
                                    supportingContent = { Text(text = item.description) },
                                    leadingContent = {
                                        Icon(
                                            modifier = Modifier.padding(start = 12.dp),
                                            imageVector = item.icon,
                                            contentDescription = null
                                        )
                                    },
                                    colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceBright)
                                )
                            }

                            is PreferenceItem.Navigation -> {
                                val color = when {
                                    item.destination == currentDestination && isExpandedLayout -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.surfaceBright
                                }
                                ListItem(
                                    modifier = Modifier
                                        .clickable { onNavigateToDetail(item.destination) },
                                    headlineContent = { Text(text = item.title) },
                                    supportingContent = { Text(text = item.description) },
                                    leadingContent = {
                                        Icon(
                                            modifier = Modifier.padding(start = 12.dp),
                                            imageVector = item.icon,
                                            contentDescription = null
                                        )
                                    },
                                    trailingContent = {
                                        Icon(
                                            imageVector = AnilibriaIcons.Outlined.ChevronRight,
                                            contentDescription = null
                                        )
                                    },
                                    colors = ListItemDefaults.colors(color)
                                )
                            }

                            is PreferenceItem.Profile -> {
                                ProfileItem(
                                    state = state,
                                    selected = PreferenceDestination.PROFILE == currentDestination && isExpandedLayout,
                                    onClick = {
                                        onNavigateToDetail(PreferenceDestination.PROFILE)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



internal sealed class PreferenceItem {
    data class Profile(
        val name: String,
        val email: String,
        val avatarUrl: String? = null,
    ) : PreferenceItem()

    data class Navigation(
        val title: String,
        val description: String,
        val icon: ImageVector,
        val destination: PreferenceDestination,
    ) : PreferenceItem()

    data class ExternalLink(
        val title: String,
        val description: String,
        val icon: ImageVector,
        val url: String,
    ) : PreferenceItem()
}

internal data class PreferenceSection(
    val title: String? = null,
    val items: List<PreferenceItem>
)

internal enum class PreferenceDestination {
    PROFILE,
    HISTORY,
    TEAM,
    DONATE,
    SETTINGS;
}

object PreferenceListDefaults {
    private val profileSection = PreferenceSection(
        title = null,
        items = listOf(
            PreferenceItem.Profile(
                name = "User name",
                email = "example@mail.com"
            )
        )
    )

    private val mainNavigationSection = PreferenceSection(
        title = "Основное",
        items = listOf(
            PreferenceItem.Navigation(
                title = "История",
                description = "Длинное описание",
                icon = AnilibriaIcons.Filled.Star,
                destination = PreferenceDestination.HISTORY
            ),
            PreferenceItem.Navigation(
                title = "Команда",
                description = "Длинное описание",
                icon = AnilibriaIcons.Filled.Star,
                destination = PreferenceDestination.TEAM
            ),
            PreferenceItem.Navigation(
                title = "Поддержать проект",
                description = "Длинное описание",
                icon = AnilibriaIcons.Filled.Star,
                destination = PreferenceDestination.DONATE
            ),
            PreferenceItem.Navigation(
                title = "Настройки",
                description = "Длинное описание",
                icon = AnilibriaIcons.Filled.Star,
                destination = PreferenceDestination.SETTINGS
            )
        )
    )

    private val externalLinksSection = PreferenceSection(
        title = "Полезные ссылки",
        items = listOf(
            PreferenceItem.ExternalLink(
                title = "GitHub",
                description = "Длинное описание",
                icon = AnilibriaIcons.Filled.Star,
                url = "https://example.com"
            ),
            PreferenceItem.ExternalLink(
                title = "YouTube",
                description = "Длинное описание",
                icon = AnilibriaIcons.Filled.Star,
                url = "https://example.com"
            ),
            PreferenceItem.ExternalLink(
                title = "Discord",
                description = "Длинное описание",
                icon = AnilibriaIcons.Filled.Star,
                url = "https://example.com"
            )
        )
    )

    internal val allSections = listOf(profileSection, mainNavigationSection, externalLinksSection)
}