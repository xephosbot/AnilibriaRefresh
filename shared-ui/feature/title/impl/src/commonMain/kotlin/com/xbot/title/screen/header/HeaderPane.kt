package com.xbot.title.screen.header

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.SingleChoiceConnectedButtonGroup
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Copyright
import com.xbot.designsystem.icons.OpenInNew
import com.xbot.designsystem.icons.PublicOff
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.CollectionType
import com.xbot.resources.Res
import com.xbot.resources.release_details_open_external_player
import com.xbot.resources.stringResource
import com.xbot.title.component.AlertCard
import com.xbot.title.component.CollectionStatusOrder
import com.xbot.title.component.FavoriteButton
import com.xbot.title.component.NotificationCard
import com.xbot.title.component.PaneHeaderScope
import com.xbot.title.component.PlayButton
import com.xbot.title.component.icon
import com.xbot.title.component.labelRes
import com.xbot.title.component.rememberPaneHeaderScope
import com.xbot.title.screen.details2.PlayButtonState
import com.xbot.title.screen.details2.ReleaseStatusBanner
import com.xbot.title.screen.details2.TitleDetailsPreviewDataV2
import org.jetbrains.compose.resources.stringResource

@Composable
context(scope: PaneHeaderScope)
internal fun HeaderPane(
    release: Release?,
    alternativeName: String?,
    playButton: PlayButtonState,
    isBlocked: Boolean,
    isFavorite: Boolean,
    collectionStatus: CollectionType?,
    statusBanner: ReleaseStatusBanner,
    onPlayClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onCollectionStatusSelect: (CollectionType?) -> Unit,
    onExternalPlayerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val horizontalPadding = PaddingValues(horizontal = 16.dp) +
        scope.contentPadding.only(WindowInsetsSides.Horizontal)

    Column(modifier = modifier) {
        LargeReleaseCard(
            modifier = Modifier.verticalParallax { scope.scrollOffset },
            contentModifier = Modifier.animateContentSize(),
            release = release,
            contentPadding = horizontalPadding
        ) { contentAlignment ->
            alternativeName?.let { name ->
                Text(
                    text = name.lines().joinToString(" "),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = if (contentAlignment == Alignment.Start) {
                        TextAlign.Start
                    } else {
                        TextAlign.Center
                    },
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            PrimaryActions(
                playButton = playButton,
                isBlocked = isBlocked,
                isFavorite = isFavorite,
                favoritesCount = release?.favoritesCount ?: 0,
                onPlayClick = onPlayClick,
                onFavoriteToggle = onFavoriteToggle
            )
        }

        CollectionStatusGroup(
            modifier = Modifier.padding(top = 10.dp),
            selected = collectionStatus,
            onSelect = onCollectionStatusSelect,
            contentPadding = horizontalPadding
        )

        StatusBanner(
            modifier = Modifier
                .padding(horizontalPadding)
                .padding(top = 10.dp),
            banner = statusBanner,
            onExternalPlayerClick = onExternalPlayerClick
        )
    }
}

@Composable
internal fun PrimaryActions(
    playButton: PlayButtonState,
    isBlocked: Boolean,
    isFavorite: Boolean,
    favoritesCount: Int,
    onPlayClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlayButton(
            modifier = Modifier.weight(1f),
            title = stringResource(playButton.title),
            subtitle = stringResource(playButton.subtitle),
            progress = playButton.progress,
            enabled = !isBlocked,
            onClick = onPlayClick
        )
        FavoriteButton(
            modifier = Modifier.fillMaxHeight(),
            checked = isFavorite,
            count = favoritesCount,
            onCheckedChange = { onFavoriteToggle() }
        )
    }
}

@Composable
internal fun StatusBanner(
    banner: ReleaseStatusBanner,
    onExternalPlayerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (banner) {
        ReleaseStatusBanner.None -> Unit

        is ReleaseStatusBanner.Blocked -> AlertCard(
            modifier = modifier,
            icon = when (banner) {
                is ReleaseStatusBanner.GeoBlocked -> AnilibertyIcons.PublicOff
                is ReleaseStatusBanner.CopyrightBlocked -> AnilibertyIcons.Copyright
            },
            title = stringResource(banner.title),
            text = banner.description?.let { stringResource(it) },
            action = if (banner.hasExternalPlayer) {
                {
                    Button(
                        onClick = onExternalPlayerClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onErrorContainer,
                            contentColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            imageVector = AnilibertyIcons.OpenInNew,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(start = ButtonDefaults.IconSpacing),
                            text = stringResource(Res.string.release_details_open_external_player)
                        )
                    }
                }
            } else {
                null
            }
        )

        is ReleaseStatusBanner.Notification -> NotificationCard(
            modifier = modifier,
            text = stringResource(banner.text)
        )
    }
}

@Composable
internal fun CollectionStatusGroup(
    selected: CollectionType?,
    onSelect: (CollectionType?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    SingleChoiceConnectedButtonGroup(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(contentPadding),
        items = CollectionStatusOrder,
        selectedItem = selected
    ) { checked, status ->
        FilledTonalToggleButton(
            checked = checked,
            onCheckedChange = { isChecked -> onSelect(if (isChecked) status else null) },
            shapes = ConnectedButtonGroupDefaults.connectedButtonShapes(
                index = CollectionStatusOrder.indexOf(status),
                count = CollectionStatusOrder.size
            )
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                imageVector = status.icon,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = ButtonDefaults.IconSpacing),
                text = stringResource(status.labelRes)
            )
        }
    }
}

@AnilibertyPreview
@Composable
private fun HeaderPanePreview() {
    val state = TitleDetailsPreviewDataV2.ongoing
    with(rememberPaneHeaderScope()) {
        HeaderPane(
            release = state.release,
            alternativeName = state.releaseDetails?.alternativeName,
            playButton = state.playButton,
            isBlocked = state.isBlocked,
            isFavorite = state.isFavorite,
            collectionStatus = state.collectionStatus,
            statusBanner = state.statusBanner,
            onPlayClick = {},
            onFavoriteToggle = {},
            onCollectionStatusSelect = {},
            onExternalPlayerClick = {}
        )
    }
}
