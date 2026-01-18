package com.xbot.preference.appearance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TonalToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.SingleChoiceConnectedButtonGroup
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.di.domainModule
import com.xbot.domain.models.AppearanceSettings
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.fixtures.di.fixturesModule
import com.xbot.localization.stringRes
import com.xbot.preference.ui.ExperimentalPill
import com.xbot.preference.ui.PreferenceItem
import com.xbot.preference.ui.SwitchPreferenceItem
import com.xbot.resources.Res
import com.xbot.resources.preference_appearance_dynamic_theme_description
import com.xbot.resources.preference_appearance_dynamic_theme_title
import com.xbot.resources.preference_appearance_expressive_color_description
import com.xbot.resources.preference_appearance_expressive_color_title
import com.xbot.resources.preference_appearance_pure_black_description
import com.xbot.resources.preference_appearance_pure_black_title
import com.xbot.resources.preference_appearance_theme_description
import com.xbot.resources.preference_appearance_theme_title
import com.xbot.resources.preference_appearance_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppearancePane(
    modifier: Modifier = Modifier,
    viewModel: AppearanceViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(stringResource(Res.string.preference_appearance_title))
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
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        AppearanceScreenContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onThemeOptionChange = {
                viewModel.onAction(AppearanceScreenAction.OnThemeOptionChange(it))
            },
            onDynamicThemeChange = {
                viewModel.onAction(AppearanceScreenAction.OnDynamicThemeChange(it))
            },
            onPureBlackChange = {
                viewModel.onAction(AppearanceScreenAction.OnPureBlackChange(it))
            },
            onExpressiveColorChange = {
                viewModel.onAction(AppearanceScreenAction.OnExpressiveColorChange(it))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AppearanceScreenContent(
    modifier: Modifier = Modifier,
    state: AppearanceSettings,
    onThemeOptionChange: (ThemeOption) -> Unit,
    onDynamicThemeChange: (Boolean) -> Unit,
    onPureBlackChange: (Boolean) -> Unit,
    onExpressiveColorChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        PreferenceItem(
            modifier = Modifier.section(0, 4),
            headlineContent = { Text(stringResource(Res.string.preference_appearance_theme_title)) },
            supportingContent = {
                Column {
                    Text(stringResource(Res.string.preference_appearance_theme_description))
                    Spacer(modifier = Modifier.height(16.dp))
                    SingleChoiceConnectedButtonGroup(
                        modifier = Modifier.fillMaxWidth(),
                        items = ThemeOption.entries,
                        selectedItem = state.themeOption
                    ) { selected, option ->
                        TonalToggleButton(
                            modifier = Modifier.weight(1f),
                            checked = selected,
                            onCheckedChange = { onThemeOptionChange(option) },
                            shapes = ConnectedButtonGroupDefaults.connectedButtonShapes(
                                index = ThemeOption.entries.indexOf(option),
                                count = ThemeOption.entries.size
                            ),
                            contentPadding = ButtonDefaults.ExtraSmallContentPadding,
                        ) {
                            Text(text = stringResource(option.stringRes))
                        }
                    }
                }
            }
        )

        // Dynamic Theme
        SwitchPreferenceItem(
            modifier = Modifier.section(1, 4),
            headlineContent = { Text(stringResource(Res.string.preference_appearance_dynamic_theme_title)) },
            supportingContent = { Text(stringResource(Res.string.preference_appearance_dynamic_theme_description)) },
            checked = state.isDynamicTheme,
            onCheckedChange = onDynamicThemeChange
        )

        // Pure Black
        SwitchPreferenceItem(
            modifier = Modifier.section(2, 4),
            headlineContent = { Text(stringResource(Res.string.preference_appearance_pure_black_title)) },
            supportingContent = { Text(stringResource(Res.string.preference_appearance_pure_black_description)) },
            checked = state.isPureBlack,
            onCheckedChange = onPureBlackChange
        )

        // Expressive color scheme
        SwitchPreferenceItem(
            modifier = Modifier.section(3, 4),
            headlineContent = { Text(stringResource(Res.string.preference_appearance_expressive_color_title)) },
            supportingContent = { Text(stringResource(Res.string.preference_appearance_expressive_color_description)) },
            checked = state.isExpressiveColor,
            badges = { ExperimentalPill() },
            onCheckedChange = onExpressiveColorChange
        )
    }
}

@Preview
@Composable
private fun AppearancePanePreview() {
    AnilibriaPreview {
        KoinApplicationPreview(
            application = {
                modules(
                    domainModule,
                    fixturesModule,
                    module {
                        single { SnackbarManager }
                        viewModelOf(::AppearanceViewModel)
                    }
                )
            }
        ) {
            AppearancePane(
                onNavigateBack = {}
            )
        }
    }
}
