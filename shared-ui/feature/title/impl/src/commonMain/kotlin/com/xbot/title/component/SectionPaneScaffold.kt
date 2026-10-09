package com.xbot.title.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculateThreePaneScaffoldValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.theme.LocalMargins

@Immutable
internal data class PaneSection<K : Any>(
    val key: K,
    val role: ThreePaneScaffoldRole,
    val title: String
)

@Stable
internal interface PaneSectionScope {
    val contentPadding: PaddingValues
    val isActive: Boolean
}

@Stable
internal interface PaneHeaderScope {
    val scrollOffset: Int
    val contentPadding: PaddingValues
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun calculateSectionPaneScaffoldDirective(
    windowAdaptiveInfo: WindowAdaptiveInfo
): PaneScaffoldDirective {
    val directive = calculatePaneScaffoldDirective(windowAdaptiveInfo)
    return if (windowAdaptiveInfo.windowPosture.isTabletop) {
        directive.copy(
            maxHorizontalPartitions = 1,
            maxVerticalPartitions = 2
        )
    } else {
        val isSplitByHinge =
            directive.excludedBounds.isNotEmpty() && directive.maxHorizontalPartitions < 2
        directive.copy(
            maxHorizontalPartitions = if (isSplitByHinge) 2 else directive.maxHorizontalPartitions,
            horizontalPartitionSpacerSize = if (isSplitByHinge) {
                24.dp
            } else {
                directive.horizontalPartitionSpacerSize
            },
            maxVerticalPartitions = 1,
            verticalPartitionSpacerSize = 0.dp
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun <K : Any> SectionPaneScaffold(
    directive: PaneScaffoldDirective,
    sections: List<PaneSection<K>>,
    selectedSection: K,
    onSectionSelect: (K) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    header: @Composable PaneHeaderScope.() -> Unit = {},
    content: @Composable PaneSectionScope.(section: K) -> Unit
) {
    val scaffoldDirective = directive.fitTo(sections)
    val scaffoldValue = calculateThreePaneScaffoldValue(
        maxHorizontalPartitions = scaffoldDirective.maxHorizontalPartitions,
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(),
        destinationHistory = emptyList(),
        maxVerticalPartitions = scaffoldDirective.maxVerticalPartitions
    )
    val layout = scaffoldValue.resolve(sections)
    val isMultiPane = scaffoldValue.hasSideBySidePanes()
    val headerState = rememberCollapsingHeaderState()
    val containerColor = MaterialTheme.colorScheme.surfaceContainer

    Column(
        modifier = modifier.background(
            if (isMultiPane) MaterialTheme.colorScheme.surface else containerColor
        )
    ) {
        if (isMultiPane) {
            topBar()
        }
        SupportingPaneScaffold(
            directive = scaffoldDirective,
            value = scaffoldValue,
            modifier = Modifier
                .weight(1f)
                .then(
                    if (isMultiPane) {
                        Modifier.windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                            )
                        )
                    } else {
                        Modifier
                    }
                ),
            mainPane = {
                SectionAnimatedPane(isMultiPane = isMultiPane) {
                    CollapsingSectionPane(
                        sections = layout.main,
                        selectedSection = selectedSection,
                        onSectionSelect = onSectionSelect,
                        headerState = headerState,
                        header = header,
                        topBar = {
                            if (!isMultiPane) {
                                Box(
                                    modifier = Modifier.background(
                                        Brush.verticalGradient(
                                            listOf(containerColor, containerColor.copy(alpha = 0f))
                                        )
                                    )
                                ) {
                                    topBar()
                                }
                            }
                        },
                        content = content
                    )
                }
            },
            supportingPane = {
                SectionAnimatedPane(
                    isMultiPane = isMultiPane,
                    modifier = Modifier.preferredHeight(0.5f)
                ) {
                    SectionTabsPane(
                        sections = layout.supporting,
                        selectedSection = selectedSection,
                        onSectionSelect = onSectionSelect,
                        content = content
                    )
                }
            },
            extraPane = {
                SectionAnimatedPane(isMultiPane = isMultiPane) {
                    SectionTabsPane(
                        sections = layout.extra,
                        selectedSection = selectedSection,
                        onSectionSelect = onSectionSelect,
                        content = content
                    )
                }
            }
        )
    }
}

@Composable
internal fun rememberPaneSectionScope(
    contentPadding: PaddingValues = PaddingValues(),
    isActive: Boolean = true
): PaneSectionScope = remember(contentPadding, isActive) {
    PaneSectionScopeImpl(contentPadding, isActive)
}

private class PaneSectionScopeImpl(
    override val contentPadding: PaddingValues,
    override val isActive: Boolean
) : PaneSectionScope

@Composable
internal fun rememberPaneHeaderScope(
    contentPadding: PaddingValues = PaddingValues()
): PaneHeaderScope = remember(contentPadding) {
    PaneHeaderScopeImpl(scrollOffset = 0, contentPadding = contentPadding)
}

private class PaneHeaderScopeImpl(
    override val scrollOffset: Int,
    override val contentPadding: PaddingValues
) : PaneHeaderScope

private class SectionLayout<K : Any>(
    val main: List<PaneSection<K>>,
    val supporting: List<PaneSection<K>>,
    val extra: List<PaneSection<K>>
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun PaneScaffoldDirective.fitTo(tabSections: List<PaneSection<*>>): PaneScaffoldDirective {
    val sidePaneCount = tabSections.map { it.role }
        .distinct()
        .count { it != SupportingPaneScaffoldRole.Main }
    return copy(
        maxHorizontalPartitions = minOf(maxHorizontalPartitions, 1 + sidePaneCount),
        maxVerticalPartitions = if (tabSections.isEmpty()) 1 else maxVerticalPartitions
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <K : Any> ThreePaneScaffoldValue.resolve(
    tabSections: List<PaneSection<K>>
): SectionLayout<K> {
    fun withRoles(vararg roles: ThreePaneScaffoldRole) = tabSections.filter { it.role in roles }

    val supportingValue = this[SupportingPaneScaffoldRole.Supporting]
    val isSupportingSideBySide = supportingValue == PaneAdaptedValue.Expanded
    val isExtraSideBySide = this[SupportingPaneScaffoldRole.Extra] == PaneAdaptedValue.Expanded
    return when {
        isSupportingSideBySide && isExtraSideBySide -> SectionLayout(
            main = withRoles(SupportingPaneScaffoldRole.Main),
            supporting = withRoles(SupportingPaneScaffoldRole.Supporting),
            extra = withRoles(SupportingPaneScaffoldRole.Extra)
        )

        isSupportingSideBySide -> SectionLayout(
            main = withRoles(SupportingPaneScaffoldRole.Main),
            supporting = withRoles(
                SupportingPaneScaffoldRole.Supporting,
                SupportingPaneScaffoldRole.Extra
            ),
            extra = emptyList()
        )

        supportingValue != PaneAdaptedValue.Hidden -> SectionLayout(
            main = emptyList(),
            supporting = tabSections,
            extra = emptyList()
        )

        else -> SectionLayout(
            main = tabSections,
            supporting = emptyList(),
            extra = emptyList()
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldValue.hasSideBySidePanes(): Boolean =
    listOf(primary, secondary, tertiary).count { it == PaneAdaptedValue.Expanded } > 1

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldPaneScope.SectionAnimatedPane(
    isMultiPane: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val containerModifier = if (isMultiPane) {
        val margin = LocalMargins.current.horizontal
        Modifier
            .paneMargins(PaddingValues(start = margin, end = margin, bottom = margin))
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    } else {
        Modifier
    }
    AnimatedPane(
        modifier = modifier
            .preferredWidth(1f)
            .then(containerModifier)
    ) {
        content()
    }
}
