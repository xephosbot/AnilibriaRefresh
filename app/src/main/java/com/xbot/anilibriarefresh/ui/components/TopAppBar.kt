package com.xbot.anilibriarefresh.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.xbot.anilibriarefresh.R

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnilibriaTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigationClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    //TODO: Реализовать проверку нахождения в top level destination
    val isTopLevel = true

    if (isTopLevel) {
        TopAppBar(
            modifier = modifier,
            navigationIcon = {
                IconButton(
                    onClick = onNavigationClick,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_anilibria),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
            scrollBehavior = scrollBehavior,
            expandedHeight = AnilibriaTopAppBarHeight
        )
    }
}

private val AnilibriaTopAppBarHeight = 56.dp