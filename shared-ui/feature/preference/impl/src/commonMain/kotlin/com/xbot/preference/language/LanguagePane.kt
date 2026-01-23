package com.xbot.preference.language

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Check
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.localization.AppLanguage
import com.xbot.localization.title
import com.xbot.resources.Res
import com.xbot.resources.preference_language_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LanguagePane(
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LanguagePaneContent(
        modifier = modifier,
        state = state,
        onLanguageSelected = viewModel::onLanguageSelected,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LanguagePaneContent(
    modifier: Modifier = Modifier,
    state: LanguageScreenState,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(stringResource(Res.string.preference_language_title))
                },
                navigationIcon = {
                    FilledTonalIconButton(
                        modifier = Modifier.padding(start = 6.dp),
                        onClick = onNavigateBack,
                        shapes = IconButtonDefaults.shapes(),
                        colors = IconButtonDefaults.filledIconButtonColors(MaterialTheme.colorScheme.surfaceContainerHighest)
                    ) {
                        Icon(
                            imageVector = AnilibriaIcons.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainer)
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
        ) {
            val languages = AppLanguage.entries
            itemsIndexed(languages) { index, language ->
                val isSelected = language == state.selectedLanguage
                PreferenceItem(
                    modifier = Modifier.section(index, languages.size),
                    headlineContent = { Text(text = stringResource(language.title)) },
                    trailingContent = {
                        if (isSelected) {
                            Icon(
                                imageVector = AnilibriaIcons.Check,
                                contentDescription = null
                            )
                        }
                    },
                    selected = isSelected,
                    onClick = { onLanguageSelected(language) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LanguagePanePreview() {
    AnilibriaPreview {
        LanguagePaneContent(
            state = LanguageScreenState(),
            onLanguageSelected = {},
            onNavigateBack = {}
        )
    }
}
