package com.xbot.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.NavigableListDetailPaneScaffold
import com.xbot.designsystem.components.isExpanded
import com.xbot.preference.donate.SupportDetailScreen
import com.xbot.preference.history.ViewHistoryDetailScreen
import com.xbot.preference.profile.ProfileDetailScreen
import com.xbot.preference.settings.SettingsDetailScreen
import com.xbot.preference.team.TeamDetailScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PreferenceScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
    onReleaseClick: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onReleaseClick = onReleaseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    onAction: (ProfileScreenAction) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<PreferenceDestination>(
        scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
            .copy(
                horizontalPartitionSpacerSize = 0.dp,
                verticalPartitionSpacerSize = 0.dp
            )
    )
    val scope = rememberCoroutineScope()
    val isTwoPaneLayout =
        scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List) && scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.Detail)
    val backBehavior = if (isTwoPaneLayout) {
        BackNavigationBehavior.PopUntilContentChange
    } else {
        BackNavigationBehavior.PopUntilScaffoldValueChange
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = scaffoldNavigator,
        defaultBackBehavior = backBehavior,
        listPane = {
            AnimatedPane {
                PreferenceListPane(
                    modifier = Modifier.preferredWidth(360.dp),
                    state = state,
                    isExpandedLayout = scaffoldNavigator.isExpanded(ThreePaneScaffoldRole.Primary),
                    currentDestination = scaffoldNavigator.currentDestination?.contentKey,
                    onNavigateToDetail = { destination ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, destination)
                        }
                    },
                    onOpenUrl = { url ->
                        println("Open url: $url")
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let { destination ->
                    when (destination) {
                        PreferenceDestination.PROFILE -> ProfileDetailScreen(
                            onNavigateBack = {
                                scope.launch {
                                    scaffoldNavigator.navigateBack()
                                }
                            }
                        )
                        PreferenceDestination.HISTORY -> ViewHistoryDetailScreen(
                            onNavigateBack = {
                                scope.launch {
                                    scaffoldNavigator.navigateBack()
                                }
                            }
                        )
                        PreferenceDestination.TEAM -> TeamDetailScreen(
                            onNavigateBack = {
                                scope.launch {
                                    scaffoldNavigator.navigateBack()
                                }
                            }
                        )
                        PreferenceDestination.DONATE -> SupportDetailScreen(
                            onNavigateBack = {
                                scope.launch {
                                    scaffoldNavigator.navigateBack()
                                }
                            }
                        )
                        PreferenceDestination.SETTINGS -> SettingsDetailScreen(
                            onNavigateBack = {
                                scope.launch {
                                    scaffoldNavigator.navigateBack()
                                }
                            }
                        )
                    }
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Выберите элемент из списка")
                    }
                }
            }
        },
    )
}