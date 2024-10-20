package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import com.xbot.anilibriarefresh.navigation.isTopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnilibriaTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigationClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    AnimatedVisibility(navController.isTopLevelDestination()) {
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