package com.xbot.profile

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.defaultDragHandleSemantics
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.NavigableListDetailPaneScaffold
import com.xbot.designsystem.components.isExpanded
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
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
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<PreferenceItem>()
    val scope = rememberCoroutineScope()
    val isTwoPaneLayout =
        scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List) && scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.Detail)
    val backBehavior = if (isTwoPaneLayout) {
        BackNavigationBehavior.PopUntilContentChange
    } else {
        BackNavigationBehavior.PopUntilScaffoldValueChange
    }

    val selectedItem = scaffoldNavigator.currentDestination?.contentKey

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        defaultBackBehavior = backBehavior,
        listPane = {
            ProfileListPane(
                modifier = Modifier.preferredWidth(360.dp),
                state = state,
                selectedItem = selectedItem,
                isTwoPaneLayout = isTwoPaneLayout,
                onPreferenceClick = { preferenceItem ->
                    scope.launch {
                        scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, preferenceItem)
                    }
                },
                onLogin = { login, password ->
                    onAction(ProfileScreenAction.Login(login, password))
                },
                onLogout = {
                    onAction(ProfileScreenAction.Logout)
                }
            )
        },
        detailPane = {
            ProfileDetailPane(
                modifier = Modifier,
                selectedItem = selectedItem,
                isTwoPaneLayout = isTwoPaneLayout,
                onReleaseClick = onReleaseClick,
                onBack = {
                    scope.launch {
                        if (scaffoldNavigator.canNavigateBack(backBehavior)) {
                            scaffoldNavigator.navigateBack(backBehavior)
                        }
                    }
                }
            )
        },
        paneExpansionState = rememberPaneExpansionState(
            keyProvider = scaffoldNavigator.scaffoldValue,
            anchors = PaneExpansionAnchors
        ),
        paneExpansionDragHandle = { state ->
            val interactionSource = remember { MutableInteractionSource() }
            VerticalDragHandle(
                modifier = Modifier.paneExpansionDraggable(
                    state = state,
                    minTouchTargetSize = LocalMinimumInteractiveComponentSize.current,
                    interactionSource = interactionSource,
                    semanticsProperties = state.defaultDragHandleSemantics()
                ),
                interactionSource = interactionSource
            )
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private val PaneExpansionAnchors =
    listOf(
        PaneExpansionAnchor.Proportion(0f),
        PaneExpansionAnchor.Offset.fromStart(360.dp),
        PaneExpansionAnchor.Proportion(0.5f),
        PaneExpansionAnchor.Offset.fromEnd(360.dp),
    )
