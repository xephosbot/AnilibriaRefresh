package com.xbot.title.screen.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.ContentCopy
import com.xbot.designsystem.icons.Link
import com.xbot.designsystem.icons.MoreVert
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.icons.Share
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.domain.models.Episode
import com.xbot.domain.models.ReleaseDetails
import com.xbot.resources.Res
import com.xbot.resources.button_share
import com.xbot.resources.button_watch
import com.xbot.resources.release_details_back
import com.xbot.resources.release_details_copy_link
import com.xbot.resources.release_details_copy_title
import com.xbot.resources.release_details_more
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun DetailsTopBar(
    title: String?,
    solid: Boolean,
    onBackClick: () -> Unit,
    onShareClick: (() -> Unit)?,
    onMoreClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        if (solid) MaterialTheme.colorScheme.surfaceContainer else Color.Transparent
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .statusBarsPadding()
            .height(TopBarHeight)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TopBarButton(
            icon = AnilibertyIcons.ArrowBack,
            contentDescription = stringResource(Res.string.release_details_back),
            solid = solid,
            onClick = onBackClick
        )
        val titleAlpha by animateFloatAsState(if (solid && title != null) 1f else 0f)
        Text(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer { alpha = titleAlpha },
            text = title.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        onShareClick?.let {
            TopBarButton(
                icon = AnilibertyIcons.Share,
                contentDescription = stringResource(Res.string.button_share),
                solid = solid,
                onClick = it
            )
        }
        onMoreClick?.let {
            TopBarButton(
                icon = AnilibertyIcons.MoreVert,
                contentDescription = stringResource(Res.string.release_details_more),
                solid = solid,
                onClick = it
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBarButton(
    icon: ImageVector,
    contentDescription: String,
    solid: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        if (solid) {
            Color.Transparent
        } else {
            MaterialTheme.colorScheme.surfaceContainer.copy(
                alpha = 0.72f
            )
        }
    )
    FilledIconButton(
        modifier = Modifier.size(TopBarButtonSize),
        onClick = onClick,
        shapes = IconButtonDefaults.shapes(),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseActionsSheet(
    details: ReleaseDetails,
    onDismiss: () -> Unit,
    onAction: (TitleDetailsActionV2) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            PreferenceItem(
                modifier = Modifier.section(0, 2),
                headlineContent = {
                    Text(text = stringResource(Res.string.release_details_copy_link))
                },
                supportingContent = { Text(text = "$RELEASE_URL_PREFIX${details.release.alias}") },
                leadingContent = {
                    Icon(imageVector = AnilibertyIcons.Link, contentDescription = null)
                },
                onClick = { onAction(TitleDetailsActionV2.OnCopyLinkClick) }
            )
            PreferenceItem(
                modifier = Modifier.section(1, 2),
                headlineContent = {
                    Text(text = stringResource(Res.string.release_details_copy_title))
                },
                leadingContent = {
                    Icon(imageVector = AnilibertyIcons.ContentCopy, contentDescription = null)
                },
                onClick = { onAction(TitleDetailsActionV2.OnCopyTextClick(details.release.name)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun LoadingContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeroHeight),
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {}
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            ContainedLoadingIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ReleaseFab(
    visible: Boolean,
    currentEpisode: Episode?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp),
            visible = visible,
            enter = scaleIn(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            ExtendedFloatingActionButton(
                onClick = onClick,
                icon = {
                    Icon(
                        imageVector = AnilibertyIcons.Filled.PlayArrow,
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = currentEpisode?.let { episodeLabel(it.ordinal) }
                            ?: stringResource(Res.string.button_watch)
                    )
                }
            )
        }
    }
}

private val TopBarButtonSize = 48.dp
private const val RELEASE_URL_PREFIX = "aniliberty.top/release/"
