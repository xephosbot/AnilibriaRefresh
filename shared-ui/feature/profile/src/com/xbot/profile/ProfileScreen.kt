package com.xbot.profile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.TelegramLogo
import com.xbot.domain.models.Profile
import com.xbot.profile.ui.PreferenceLayout
import com.xbot.profile.ui.ProfileItem
import com.xbot.profile.ui.preferenceItem
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    onAction: (ProfileScreenAction) -> Unit
) {
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
        Crossfade(
            targetState = state,
        ) { targetState ->
            when (targetState) {
                is ProfileScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                is ProfileScreenState.LoggedIn -> {
                    LoggedInScreen(
                        profile = targetState.userProfile,
                        contentPadding = innerPadding,
                        onLogout = { onAction(ProfileScreenAction.Logout) }
                    )
                }

                is ProfileScreenState.LoggedOut -> {
                    LoggedOutScreen(
                        contentPadding = innerPadding,
                        onLogin = { login, password ->
                            onAction(ProfileScreenAction.Login(login, password))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoggedInScreen(
    modifier: Modifier = Modifier,
    profile: Profile,
    contentPadding: PaddingValues,
    onLogout: () -> Unit,
) {
    PreferenceLayout(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        section {
            item {
                ProfileItem(
                    profile = profile,
                    onAccountClick = {

                    },
                    onLogoutClick = {

                    }
                )
            }
        }

        section {
            preferenceItem(
                title = "История",
                description = "Список просмотренных аниме",
                //FIXME: Add History icon
                icon = AnilibriaIcons.Filled.Star,
            ) {
                // Переход к истории
            }
            preferenceItem(
                title = "Команда проекта",
                description = "Разработчики и контрибьюторы",
                //FIXME: Add Group icon
                icon = AnilibriaIcons.Filled.Star,
            ) {
                // О команде
            }
            preferenceItem(
                title = "Поддержать",
                description = "Донаты и способы помощи",
                //FIXME: Add Redeem icon
                icon = AnilibriaIcons.Filled.Star,
            ) {
                // Поддержка проекта
            }
            preferenceItem(
                title = "Настройки",
                description = "Внешний вид и уведомления",
                //FIXME: Add Settings icon
                icon = AnilibriaIcons.Filled.Star,
            ) {
                // Основные настройки
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

@Composable
private fun LoggedOutScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onLogin: (String, String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text("Logged Out")
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
