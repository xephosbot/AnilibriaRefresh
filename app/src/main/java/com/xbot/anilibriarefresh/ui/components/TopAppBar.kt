package com.xbot.anilibriarefresh.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.navigation.isTopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnilibriaTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigationClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val titleArg = navBackStackEntry?.arguments?.getString("titleName")
    val isTopLevel = navController.isTopLevelDestination()

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = {
                    when (isTopLevel) {
                        true -> onNavigationClick()
                        else -> navController.navigateUp()
                    }
                },
            ) {
                when (isTopLevel) {
                    true -> Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_anilibria),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    else -> Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            Text(
                text = when (isTopLevel) {
                    true -> stringResource(R.string.app_name)
                    else -> titleArg ?: "Unknown"
                },
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
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

private val AnilibriaTopAppBarHeight = 56.dp