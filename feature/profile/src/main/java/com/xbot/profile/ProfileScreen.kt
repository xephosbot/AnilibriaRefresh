package com.xbot.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.TelegramLogo
import com.xbot.domain.models.Profile
import org.koin.androidx.compose.koinViewModel

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
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
    ) {
        var showLogoutItem by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceBright)
        ) {
            ProfileItem(
                profile = profile,
                onClick = {
                    showLogoutItem = !showLogoutItem
                }
            )
            AnimatedVisibility(showLogoutItem) {
                SettingItem(
                    title = "Выход",
                    icon = Icons.AutoMirrored.Outlined.ExitToApp,
                    onClick = { onLogout() }
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
            SettingItem(
                title = "История",
                icon = Icons.Outlined.History,
                onClick = {}
            )
            SettingItem(
                title = "Команда проекта",
                icon = Icons.Outlined.Group,
                onClick = {}
            )
            SettingItem(
                title = "Поддержать",
                icon = Icons.Outlined.Redeem,
                onClick = {}
            )
            SettingItem(
                title = "Настройки",
                icon = Icons.Outlined.Settings,
                onClick = {}
            )
        }

        SettingItem(
            title = "Группа ВК",
            icon = AnilibriaIcons.Filled.Star,
            contentPadding = PaddingValues(horizontal = 24.dp),
            onClick = {}
        )
        SettingItem(
            title = "Канал YouTube",
            icon = AnilibriaIcons.Filled.Star,
            contentPadding = PaddingValues(horizontal = 24.dp),
            onClick = {}
        )
        SettingItem(
            title = "Patreon",
            icon = AnilibriaIcons.Filled.Star,
            contentPadding = PaddingValues(horizontal = 24.dp),
            onClick = {}
        )
        SettingItem(
            title = "Канал Telegram",
            icon = AnilibriaIcons.Filled.TelegramLogo,
            contentPadding = PaddingValues(horizontal = 24.dp),
            onClick = {}
        )
        SettingItem(
            title = "Чат Discord",
            icon = AnilibriaIcons.Filled.Star,
            contentPadding = PaddingValues(horizontal = 24.dp),
            onClick = {}
        )
    }
}

@Composable
private fun ProfileItem(
    profile: Profile,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterImage(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            poster = profile.avatar
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = profile.nickname ?: "ID: ${profile.id}",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                modifier = Modifier.graphicsLayer { alpha = 0.6f },
                text = profile.email,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.width(12.dp))
        Spacer(Modifier.weight(1f))

        Icon(
            modifier = Modifier.border(
                width = 1.dp,
                color = Color.White,
                shape = CircleShape
            ),
            imageVector = Icons.Outlined.ArrowDropDown,
            contentDescription = null
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )

        Spacer(Modifier.width(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
        )
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