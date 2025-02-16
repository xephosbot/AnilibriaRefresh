package com.xbot.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.xbot.designsystem.icons.AnilibriaIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLayout(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.systemBars,
    content: @Composable (Boolean) -> Unit,
) {
    var active by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize()
            .windowInsetsPadding(windowInsets)
    ) {
        SearchBarDefaults.InputField(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch()
                active = false
            },
            expanded = active,
            onExpandedChange = { active = it },
            leadingIcon = {
                androidx.compose.material3.IconButton(
                    onClick = { onBack() }
                ) {
                    Icon(
                        imageVector = AnilibriaIcons.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                androidx.compose.material3.IconButton(
                    onClick = { onQueryChange(String()) }
                ) {
                    Icon(
                        imageVector = AnilibriaIcons.Outlined.Clear,
                        contentDescription = null
                    )
                }
            }
        )
        HorizontalDivider()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            content = { content(active) }
        )
    }
}