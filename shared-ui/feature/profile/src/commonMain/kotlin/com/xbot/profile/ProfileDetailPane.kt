package com.xbot.profile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun ThreePaneScaffoldPaneScope.ProfileDetailPane(
    modifier: Modifier = Modifier,
    selectedItem: PreferenceItem?,
    isTwoPaneLayout: Boolean,
    onReleaseClick: (Int) -> Unit,
    onBack: () -> Unit,
) {
    AnimatedPane(modifier = modifier) {
        Scaffold(
            modifier = Modifier
                .padding(
                    top = if (isTwoPaneLayout) 16.dp else 0.dp,
                    bottom = if (isTwoPaneLayout) 16.dp else 0.dp,
                    start = 0.dp,
                    end = if (isTwoPaneLayout) 16.dp else 0.dp,
                )
                .clip(RoundedCornerShape(if (isTwoPaneLayout) 24.dp else 0.dp)),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(text = selectedItem?.name?.lowercase()?.replaceFirstChar { it.uppercaseChar() }.orEmpty())
                    },
                    navigationIcon = {
                        if (!isTwoPaneLayout) {
                            FilledIconButton(onClick = onBack) {
                                Icon(
                                    imageVector = AnilibriaIcons.Outlined.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ) { innerPadding ->
            Crossfade(
                targetState = selectedItem
            ) { targetState ->
                when (targetState) {
                    PreferenceItem.PROFILE -> {
                        ProfileScreen(contentPadding = innerPadding)
                        //Box(modifier = Modifier.fillMaxSize().background(Color.Magenta)) {}
                    }

                    PreferenceItem.HISTORY -> {
                        //Box(modifier = Modifier.fillMaxSize().background(Color.Green)) {}
                        HistoryScreen(
                            contentPadding = innerPadding,
                            onReleaseClick = onReleaseClick
                        )
                    }

                    PreferenceItem.TEAM -> {
                        //Box(modifier = Modifier.fillMaxSize().background(Color.Red)) {}
                    }

                    PreferenceItem.DONATE -> {
                        //Box(modifier = Modifier.fillMaxSize().background(Color.Blue)) {}
                    }

                    PreferenceItem.SETTINGS -> {
                        //Box(modifier = Modifier.fillMaxSize().background(Color.Yellow)) {}
                    }

                    else -> {
                        LoadingScreen(contentPadding = PaddingValues(0.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            label = { Text("Email") },
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            label = { Text("Password") },
        )
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