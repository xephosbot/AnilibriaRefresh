@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.anilibriarefresh.ui.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirst
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.navigation.NavigationContentPosition
import com.xbot.anilibriarefresh.navigation.TOP_LEVEL_DESTINATIONS
import com.xbot.anilibriarefresh.navigation.TopLevelDestination
import com.xbot.anilibriarefresh.navigation.hasRoute
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun AnilibriaNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navContentPosition: NavigationContentPosition = NavigationContentPosition.TOP,
    destinations: List<TopLevelDestination> = TOP_LEVEL_DESTINATIONS
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationRail(
        modifier = modifier,
        header = {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_anilibria),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination.hasRoute(destination)
            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        //TODO: исправить imageVector
                        imageVector = ImageVector.vectorResource(destination.iconCurrent),
                        contentDescription = destination.text
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationRail(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    windowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout).only(
        WindowInsetsSides.Vertical + WindowInsetsSides.Start
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier,
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .windowInsetsPadding(windowInsets)
                .widthIn(min = 80.0.dp)
                .padding(vertical = NavigationRailPadding)
                .selectableGroup(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (header != null) {
                header()
                Spacer(Modifier.height(HeaderPadding))
            }
            content()
        }
    }
}

@Composable
fun NavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    colors: NavigationRailItemColors = NavigationRailItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f)
    ),
    interactionSource: MutableInteractionSource? = null
) {
    val styledLabel: @Composable (() -> Unit)? =
        label?.let {
            @Composable {
                val style =
                    androidx.compose.material.MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center)
                androidx.compose.material.ProvideTextStyle(style, content = label)
            }
        }
    // Default to compact size when the item has no label, or a regular size when it does.
    // Any size value that was set on the given Modifier will take precedence and allow custom
    // sizing.
    val itemSize = if (label == null) NavigationRailItemCompactSize else NavigationRailItemSize
    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = ripple(bounded = false)
            )
            .size(itemSize),
        contentAlignment = Alignment.Center
    ) {
        NavigationRailTransition(
            activeColor = colors.selectedIconColor,
            inactiveColor = colors.unselectedIconColor,
            selected = selected
        ) { progress ->
            val animationProgress = if (alwaysShowLabel) 1f else progress

            NavigationRailItemBaselineLayout(
                icon = icon,
                label = styledLabel,
                iconPositionAnimationProgress = animationProgress
            )
        }
    }
}

@Composable
private fun NavigationRailTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable (animationProgress: Float) -> Unit
) {
    val animationProgress by
    animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = NavigationRailAnimationSpec
    )

    val color = lerp(inactiveColor, activeColor, animationProgress)

    CompositionLocalProvider(LocalContentColor provides color) {
        content(animationProgress)
    }
}

@Composable
private fun NavigationRailItemBaselineLayout(
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)?,
    @FloatRange(from = 0.0, to = 1.0) iconPositionAnimationProgress: Float
) {
    Layout({
        Box(Modifier.layoutId("icon")) { icon() }
        if (label != null) {
            Box(
                Modifier
                    .layoutId("label")
                    .alpha(iconPositionAnimationProgress)) { label() }
        }
    }) { measurables, constraints ->
        val iconPlaceable = measurables.fastFirst { it.layoutId == "icon" }.measure(constraints)

        val labelPlaceable =
            label?.let {
                measurables
                    .fastFirst { it.layoutId == "label" }
                    .measure(
                        // Measure with loose constraints for height as we don't want the label to
                        // take up more
                        // space than it needs
                        constraints.copy(minHeight = 0)
                    )
            }

        // If there is no label, just place the icon.
        if (label == null) {
            placeIcon(iconPlaceable, constraints)
        } else {
            placeLabelAndIcon(
                labelPlaceable!!,
                iconPlaceable,
                constraints,
                iconPositionAnimationProgress
            )
        }
    }
}

private fun MeasureScope.placeIcon(
    iconPlaceable: Placeable,
    constraints: Constraints
): MeasureResult {
    val iconX = max(0, (constraints.maxWidth - iconPlaceable.width) / 2)
    val iconY = max(0, (constraints.maxHeight - iconPlaceable.height) / 2)
    return layout(constraints.maxWidth, constraints.maxHeight) {
        iconPlaceable.placeRelative(iconX, iconY)
    }
}

private fun MeasureScope.placeLabelAndIcon(
    labelPlaceable: Placeable,
    iconPlaceable: Placeable,
    constraints: Constraints,
    @FloatRange(from = 0.0, to = 1.0) iconPositionAnimationProgress: Float
): MeasureResult {
    val baseline = labelPlaceable[LastBaseline]
    val labelBaselineOffset = ItemLabelBaselineBottomOffset.roundToPx()
    // Label should be [ItemLabelBaselineBottomOffset] from the bottom
    val labelY = constraints.maxHeight - baseline - labelBaselineOffset
    val labelX = (constraints.maxWidth - labelPlaceable.width) / 2

    // Icon should be [ItemIconTopOffset] from the top when selected
    val selectedIconY = ItemIconTopOffset.roundToPx()
    val unselectedIconY = (constraints.maxHeight - iconPlaceable.height) / 2
    val iconX = (constraints.maxWidth - iconPlaceable.width) / 2
    // How far the icon needs to move between unselected and selected states
    val iconDistance = unselectedIconY - selectedIconY

    // When selected the icon is above the unselected position, so we will animate moving
    // downwards from the selected state, so when progress is 1, the total distance is 0, and we
    // are at the selected state.
    val offset = (iconDistance * (1 - iconPositionAnimationProgress)).roundToInt()

    return layout(constraints.maxWidth, constraints.maxHeight) {
        if (iconPositionAnimationProgress != 0f) {
            labelPlaceable.placeRelative(labelX, labelY + offset)
        }
        iconPlaceable.placeRelative(iconX, selectedIconY + offset)
    }
}

private val NavigationRailAnimationSpec =
    TweenSpec<Float>(durationMillis = 300, easing = FastOutSlowInEasing)

private val NavigationRailItemSize = 72.dp

private val NavigationRailItemCompactSize = 56.dp

private val NavigationRailPadding = 8.dp

private val HeaderPadding = 8.dp

private val ItemLabelBaselineBottomOffset = 16.dp

private val ItemIconTopOffset = 14.dp
