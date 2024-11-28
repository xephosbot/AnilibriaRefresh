@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.anilibriarefresh.ui.components

import android.content.res.Resources
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapNotNull
import androidx.compose.ui.util.fastMaxBy
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.resources
import com.xbot.anilibriarefresh.ui.utils.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
    ) {
        ScaffoldLayout(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.systemBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal),
            ),
            fabPosition = floatingActionButtonPosition,
            topBar = topBar,
            bottomBar = bottomBar,
            content = content,
            snackbar = {
                SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        shape = MaterialTheme.shapes.medium,
                    )
                }
            },
            contentWindowInsets = contentWindowInsets,
            fab = floatingActionButton,
        )
    }
}

@Composable
private fun ScaffoldLayout(
    modifier: Modifier,
    fabPosition: FabPosition,
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    snackbar: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    contentWindowInsets: WindowInsets,
    bottomBar: @Composable () -> Unit,
) {
    val navigationPaddingBottom = LocalNavigationPadding.current.calculateBottomPadding()
        .takeIf { it != 0.dp }

    SubcomposeLayout(modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val topBarPlaceables = subcompose(ScaffoldLayoutContent.TopBar, topBar).fastMap {
            it.measure(looseConstraints)
        }

        val topBarHeight = topBarPlaceables.fastMaxBy { it.height }?.height ?: 0

        val snackbarPlaceables = subcompose(ScaffoldLayoutContent.Snackbar, snackbar).fastMap {
            // respect only bottom and horizontal for snackbar and fab
            val leftInset = contentWindowInsets
                .getLeft(this@SubcomposeLayout, layoutDirection)
            val rightInset = contentWindowInsets
                .getRight(this@SubcomposeLayout, layoutDirection)
            val bottomInset = contentWindowInsets.getBottom(this@SubcomposeLayout)
            // offset the snackbar constraints by the insets values
            it.measure(
                looseConstraints.offset(
                    -leftInset - rightInset,
                    -bottomInset,
                ),
            )
        }

        val snackbarHeight = snackbarPlaceables.fastMaxBy { it.height }?.height ?: 0
        val snackbarWidth = snackbarPlaceables.fastMaxBy { it.width }?.width ?: 0

        val fabPlaceables =
            subcompose(ScaffoldLayoutContent.Fab, fab).fastMapNotNull { measurable ->
                // respect only bottom and horizontal for snackbar and fab
                val leftInset =
                    contentWindowInsets.getLeft(this@SubcomposeLayout, layoutDirection)
                val rightInset =
                    contentWindowInsets.getRight(this@SubcomposeLayout, layoutDirection)
                val bottomInset = contentWindowInsets.getBottom(this@SubcomposeLayout)
                measurable.measure(
                    looseConstraints.offset(
                        -leftInset - rightInset,
                        -bottomInset,
                    ),
                )
                    .takeIf { it.height != 0 && it.width != 0 }
            }

        val fabPlacement = if (fabPlaceables.isNotEmpty()) {
            val fabWidth = fabPlaceables.fastMaxBy { it.width }!!.width
            val fabHeight = fabPlaceables.fastMaxBy { it.height }!!.height
            // FAB distance from the left of the layout, taking into account LTR / RTL
            val fabLeftOffset = if (fabPosition == FabPosition.End) {
                if (layoutDirection == LayoutDirection.Ltr) {
                    layoutWidth - FabSpacing.roundToPx() - fabWidth
                } else {
                    FabSpacing.roundToPx()
                }
            } else {
                (layoutWidth - fabWidth) / 2
            }

            FabPlacement(
                left = fabLeftOffset,
                width = fabWidth,
                height = fabHeight,
            )
        } else {
            null
        }

        val bottomBarPlaceables = subcompose(ScaffoldLayoutContent.BottomBar) {
            CompositionLocalProvider(
                LocalFabPlacement provides fabPlacement,
                content = bottomBar,
            )
        }.map { it.measure(looseConstraints) }

        val bottomBarHeight = bottomBarPlaceables.maxByOrNull { it.height }?.height
            ?: navigationPaddingBottom?.roundToPx()
        val fabOffsetFromBottom = fabPlacement?.let {
            if (bottomBarHeight == null) {
                it.height + FabSpacing.roundToPx() +
                    contentWindowInsets.getBottom(this@SubcomposeLayout)
            } else {
                // Total height is the bottom bar height + the FAB height + the padding
                // between the FAB and bottom bar
                bottomBarHeight + it.height + FabSpacing.roundToPx()
            }
        }

        val snackbarOffsetFromBottom = if (snackbarHeight != 0) {
            snackbarHeight +
                (
                    fabOffsetFromBottom ?: bottomBarHeight
                        ?: contentWindowInsets.getBottom(this@SubcomposeLayout)
                )
        } else {
            0
        }

        val bodyContentPlaceables = subcompose(ScaffoldLayoutContent.MainContent) {
            val insets = contentWindowInsets.asPaddingValues(this@SubcomposeLayout)
            val innerPadding = PaddingValues(
                top = if (topBarPlaceables.isEmpty()) {
                    insets.calculateTopPadding()
                } else {
                    topBarHeight.toDp()
                },
                bottom = if (bottomBarPlaceables.isEmpty() || bottomBarHeight == null) {
                    insets.calculateBottomPadding()
                } else {
                    bottomBarHeight.toDp()
                },
                start = insets.calculateStartPadding((this@SubcomposeLayout).layoutDirection),
                end = insets.calculateEndPadding((this@SubcomposeLayout).layoutDirection),
            )
            content(innerPadding)
        }.fastMap {
            it.measure(looseConstraints)
        }

        layout(layoutWidth, layoutHeight) {
            // Placing to control drawing order to match default elevation of each placeable

            bodyContentPlaceables.fastForEach {
                it.place(0, 0)
            }
            topBarPlaceables.fastForEach {
                it.place(0, 0)
            }
            snackbarPlaceables.fastForEach {
                it.place(
                    (layoutWidth - snackbarWidth) / 2 +
                        contentWindowInsets.getLeft(this@SubcomposeLayout, layoutDirection),
                    layoutHeight - snackbarOffsetFromBottom,
                )
            }
            // The bottom bar is always at the bottom of the layout
            bottomBarPlaceables.fastForEach {
                it.place(0, layoutHeight - (bottomBarHeight ?: 0))
            }
            // Explicitly not using placeRelative here as `leftOffset` already accounts for RTL
            fabPlacement?.let { placement ->
                fabPlaceables.fastForEach {
                    it.place(placement.left, layoutHeight - fabOffsetFromBottom!!)
                }
            }
        }
    }
}

/**
 * Remember and creates an instance of [ScaffoldState]
 */
@Composable
fun rememberScaffoldState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ScaffoldState = remember(snackbarHostState, snackbarManager, resources, coroutineScope) {
    ScaffoldState(snackbarHostState, snackbarManager, resources, coroutineScope)
}

@Stable
class ScaffoldState(
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.stringResource(message.title)
                    val actionLabel = message.action?.title?.let { resources.stringResource(it) }
                    // Notify the SnackbarManager so it can remove the current message from the list
                    snackbarManager.setMessageShown(message.id)
                    // Display the snackbar on the screen. `showSnackbar` is a function
                    // that suspends until the snackbar disappears from the screen
                    val result = snackbarHostState.showSnackbar(
                        message = text,
                        actionLabel = actionLabel,
                    )

                    when (result) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> message.action?.action?.let { it() }
                    }
                }
            }
        }
    }
}

/**
 * Placement information for a [FloatingActionButton] inside a [Scaffold].
 *
 * @property left the FAB's offset from the left edge of the bottom bar, already adjusted for RTL
 * support
 * @property width the width of the FAB
 * @property height the height of the FAB
 */
@Immutable
internal class FabPlacement(
    val left: Int,
    val width: Int,
    val height: Int,
)

/**
 * CompositionLocal containing a [FabPlacement] that is used to calculate the FAB bottom offset.
 */
internal val LocalFabPlacement = staticCompositionLocalOf<FabPlacement?> { null }

// FAB spacing above the bottom bar / bottom of the Scaffold
private val FabSpacing = 16.dp

private enum class ScaffoldLayoutContent { TopBar, MainContent, Snackbar, Fab, BottomBar }
