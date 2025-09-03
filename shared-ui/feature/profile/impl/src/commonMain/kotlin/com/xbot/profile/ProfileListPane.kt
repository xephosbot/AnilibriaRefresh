package com.xbot.profile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.PreferenceLayout
import com.xbot.designsystem.components.preferenceItem
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.TelegramLogo
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerSafe
import com.xbot.designsystem.modifier.shimmerUpdater

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun ThreePaneScaffoldPaneScope.ProfileListPane(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    isTwoPaneLayout: Boolean,
    selectedItem: PreferenceItem?,
    onPreferenceClick: (PreferenceItem) -> Unit,
    onLogin: (String, String) -> Unit,
    onLogout: () -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    AnimatedPane(modifier = modifier) {
        ProvideShimmer(shimmer) {
            PreferenceLayout(
                modifier = modifier.shimmerUpdater(shimmer),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = if (isTwoPaneLayout) 0.dp else 16.dp,
                )
            ) {
                section {
                    item {
                        ProfileItem(
                            state = state,
                            selected = selectedItem == PreferenceItem.PROFILE && isTwoPaneLayout,
                        ) {
                            onPreferenceClick(PreferenceItem.PROFILE)
                        }
                    }
                }
                section {
                    preferenceItem(
                        title = "История",
                        description = "Список просмотренных аниме",
                        //FIXME: Add History icon
                        icon = AnilibriaIcons.Filled.Star,
                        selected = selectedItem == PreferenceItem.HISTORY && isTwoPaneLayout,
                    ) {
                        onPreferenceClick(PreferenceItem.HISTORY)
                    }
                    preferenceItem(
                        title = "Команда проекта",
                        description = "Разработчики и контрибьюторы",
                        //FIXME: Add Group icon
                        icon = AnilibriaIcons.Filled.Star,
                        selected = selectedItem == PreferenceItem.TEAM && isTwoPaneLayout,
                    ) {
                        onPreferenceClick(PreferenceItem.TEAM)
                    }
                    preferenceItem(
                        title = "Поддержать",
                        description = "Донаты и способы помощи",
                        //FIXME: Add Redeem icon
                        icon = AnilibriaIcons.Filled.Star,
                        selected = selectedItem == PreferenceItem.DONATE && isTwoPaneLayout,
                    ) {
                        onPreferenceClick(PreferenceItem.DONATE)
                    }
                    preferenceItem(
                        title = "Настройки",
                        description = "Внешний вид и уведомления",
                        //FIXME: Add Settings icon
                        icon = AnilibriaIcons.Filled.Star,
                        selected = selectedItem == PreferenceItem.SETTINGS && isTwoPaneLayout,
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
}

@Composable
private fun ProfileItem(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceBright
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 76.dp)
            .clickable(onClick = onClick)
            .padding(start = 14.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Crossfade(targetState = state) { targetState ->
            when (targetState) {
                ProfileScreenState.Loading -> {
                    Row(
                        modifier = Modifier.shimmerSafe(shimmer),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                        )
                        Spacer(Modifier.width(12.dp))

                        Column {
                            Box(
                                modifier = Modifier
                                    .height(16.dp)
                                    .widthIn(max = 300.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray),
                            )
                            Spacer(Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .height(16.dp)
                                    .widthIn(max = 260.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray),
                            )
                        }
                    }
                }

                is ProfileScreenState.LoggedIn -> {
                    Row {
                        PosterImage(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape),
                            poster = targetState.userProfile.avatar
                        )
                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                text = targetState.userProfile.nickname ?: "ID: ${targetState.userProfile.id}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                modifier = Modifier.graphicsLayer { alpha = 0.6f },
                                text = targetState.userProfile.email,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }
                }

                ProfileScreenState.LoggedOut -> {
                    Row {
                        PosterImage(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape),
                            poster = null
                        )
                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Вход в аккаунт",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                modifier = Modifier.graphicsLayer { alpha = 0.6f },
                                text = "Войдите в аккаунт",
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}


internal enum class PreferenceItem(
    val id: Int,
) {
    PROFILE(0),
    HISTORY(1),
    TEAM(2),
    DONATE(3),
    SETTINGS(4);
}

