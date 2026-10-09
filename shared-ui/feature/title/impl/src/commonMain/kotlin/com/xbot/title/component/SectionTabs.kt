package com.xbot.title.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
internal fun <K : Any> SectionTabsPane(
    sections: List<PaneSection<K>>,
    selectedSection: K,
    onSectionSelect: (K) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable PaneSectionScope.(section: K) -> Unit
) {
    if (sections.isEmpty()) return

    val pagerState = rememberSectionPagerState(sections, selectedSection, onSectionSelect)

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            )
    ) {
        if (sections.size > 1) {
            SectionTabRow(sections = sections, pagerState = pagerState)
        }
        SectionPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            sections = sections,
            pagerState = pagerState,
            pageModifier = { Modifier.fillMaxSize() },
            content = content
        )
    }
}

@Composable
internal fun <K : Any> SectionTabRow(
    sections: List<PaneSection<K>>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val selectedIndex = pagerState.currentPage
    PrimaryTabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedIndex, matchContentSize = true)
            )
        }
    ) {
        sections.forEachIndexed { index, section ->
            Tab(
                selected = index == selectedIndex,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                text = {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

@Composable
internal fun <K : Any> SectionPager(
    sections: List<PaneSection<K>>,
    pagerState: PagerState,
    pageModifier: (K) -> Modifier,
    modifier: Modifier = Modifier,
    pageContentPadding: PaddingValues = PaddingValues(),
    content: @Composable PaneSectionScope.(section: K) -> Unit
) {
    key(pagerState) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
        ) { index ->
            val section = sections[index].key
            val scope = rememberPaneSectionScope(
                contentPadding = pageContentPadding,
                isActive = pagerState.settledPage == index
            )
            Box(modifier = pageModifier(section)) {
                scope.content(section)
            }
        }
    }
}

@Composable
internal fun <K : Any> rememberSectionPagerState(
    sections: List<PaneSection<K>>,
    selectedSection: K,
    onSectionSelect: (K) -> Unit
): PagerState {
    val keys = sections.map { it.key }
    val pagerState = key(keys) {
        rememberPagerState(initialPage = keys.indexOf(selectedSection).coerceAtLeast(0)) {
            keys.size
        }
    }
    val currentOnSectionSelect by rememberUpdatedState(onSectionSelect)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .drop(1)
            .collect { index -> currentOnSectionSelect(keys[index]) }
    }
    LaunchedEffect(pagerState, selectedSection) {
        val index = keys.indexOf(selectedSection)
        if (index >= 0 && index != pagerState.currentPage && !pagerState.isScrollInProgress) {
            pagerState.scrollToPage(index)
        }
    }
    return pagerState
}
