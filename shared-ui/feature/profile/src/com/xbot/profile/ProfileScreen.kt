package com.xbot.profile

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.defaultDragHandleSemantics
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.NavigableListDetailPaneScaffold
import com.xbot.designsystem.components.isExpanded
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.TelegramLogo
import com.xbot.designsystem.components.PreferenceLayout
import com.xbot.designsystem.components.preferenceItem
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    onAction: (ProfileScreenAction) -> Unit
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<PreferenceItem>()
    val scope = rememberCoroutineScope()
    val backBehavior =
        if (scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List) && scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.Detail)) {
            BackNavigationBehavior.PopUntilContentChange
        } else {
            BackNavigationBehavior.PopUntilScaffoldValueChange
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Профиль")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        val selectedItem = scaffoldNavigator.currentDestination?.contentKey

        NavigableListDetailPaneScaffold(
            navigator = scaffoldNavigator,
            defaultBackBehavior = backBehavior,
            listPane = {
                ProfileListPane(
                    modifier = Modifier.preferredWidth(360.dp),
                    state = state,
                    selectedItem = selectedItem,
                    isListExpanded = scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                    isDetailsExpanded = scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.Detail),
                    contentPadding = innerPadding,
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
                    isDetailsExpanded = scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.Detail),
                    contentPadding = innerPadding,
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
                        state,
                        LocalMinimumInteractiveComponentSize.current,
                        interactionSource,
                        state.defaultDragHandleSemantics()
                    ),
                    interactionSource = interactionSource
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldPaneScope.ProfileListPane(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    selectedItem: PreferenceItem?,
    isListExpanded: Boolean,
    isDetailsExpanded: Boolean,
    contentPadding: PaddingValues,
    onPreferenceClick: (PreferenceItem) -> Unit,
    onLogin: (String, String) -> Unit,
    onLogout: () -> Unit,
) {
    AnimatedPane(modifier = modifier) {
        PreferenceLayout(contentPadding = contentPadding) {
            section {
                preferenceItem(
                    title = "История",
                    description = "Список просмотренных аниме",
                    //FIXME: Add History icon
                    icon = AnilibriaIcons.Filled.Star,
                    selected = selectedItem == PreferenceItem.HISTORY && isListExpanded,
                ) {
                    onPreferenceClick(PreferenceItem.HISTORY)
                }
                preferenceItem(
                    title = "Команда проекта",
                    description = "Разработчики и контрибьюторы",
                    //FIXME: Add Group icon
                    icon = AnilibriaIcons.Filled.Star,
                    selected = selectedItem == PreferenceItem.TEAM && isListExpanded,
                ) {
                    onPreferenceClick(PreferenceItem.TEAM)
                }
                preferenceItem(
                    title = "Поддержать",
                    description = "Донаты и способы помощи",
                    //FIXME: Add Redeem icon
                    icon = AnilibriaIcons.Filled.Star,
                    selected = selectedItem == PreferenceItem.DONATE && isListExpanded,
                ) {
                    onPreferenceClick(PreferenceItem.DONATE)
                }
                preferenceItem(
                    title = "Настройки",
                    description = "Внешний вид и уведомления",
                    //FIXME: Add Settings icon
                    icon = AnilibriaIcons.Filled.Star,
                    selected = selectedItem == PreferenceItem.SETTINGS && isListExpanded,
                ) {
                    onPreferenceClick(PreferenceItem.SETTINGS)
                }
            }

            section {
                preferenceItem(
                    title = "Группа ВК",
                    description = "Официальная группа ВКонтакте",
                    icon = AnilibriaIcons.Filled.Star,
                ) {
                    // Ссылка на ВК
                }
                preferenceItem(
                    title = "Канал YouTube",
                    description = "Авторские видео и анонсы",
                    icon = AnilibriaIcons.Filled.Star,
                ) {
                    // Ссылка на YouTube
                }
                preferenceItem(
                    title = "Patreon",
                    description = "Поддержать проект деньгами",
                    icon = AnilibriaIcons.Filled.Star,
                ) {
                    // Ссылка на Patreon
                }
                preferenceItem(
                    title = "Канал Telegram",
                    description = "Новости и обсуждения",
                    icon = AnilibriaIcons.Filled.TelegramLogo,
                ) {
                    // Ссылка на Telegram
                }
                preferenceItem(
                    title = "Чат Discord",
                    description = "Общение с сообществом",
                    icon = AnilibriaIcons.Filled.Star,
                ) {
                    // Ссылка на Discord
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldPaneScope.ProfileDetailPane(
    modifier: Modifier = Modifier,
    selectedItem: PreferenceItem?,
    isDetailsExpanded: Boolean,
    contentPadding: PaddingValues,
    onBack: () -> Unit,
) {
    AnimatedPane(modifier = modifier) {
        Crossfade(
            targetState = selectedItem,
        ) { targetState ->
            when (targetState) {
                PreferenceItem.HISTORY -> HistoryScreen(
                    contentPadding = contentPadding
                )

                PreferenceItem.TEAM -> {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Red)) {}
                }

                PreferenceItem.DONATE -> {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Blue)) {}
                }

                PreferenceItem.SETTINGS -> {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Yellow)) {}
                }

                else -> {
                    LoadingScreen(contentPadding = PaddingValues(0.dp))
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private enum class PreferenceItem(
    val id: Int,
) {
    PROFILE(0),
    HISTORY(1),
    TEAM(2),
    DONATE(3),
    SETTINGS(4);
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private val PaneExpansionAnchors =
    listOf(
        PaneExpansionAnchor.Proportion(0f),
        PaneExpansionAnchor.Offset.fromStart(360.dp),
        PaneExpansionAnchor.Proportion(0.5f),
        PaneExpansionAnchor.Offset.fromEnd(360.dp),
    )
