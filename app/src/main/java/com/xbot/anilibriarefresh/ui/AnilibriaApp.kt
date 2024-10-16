package com.xbot.anilibriarefresh.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.navigation.AnilibriaNavGraph
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationBar
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationRail
import com.xbot.anilibriarefresh.navigation.AnilibriaNavigationSuiteScaffold
import com.xbot.anilibriarefresh.ui.components.Scaffold
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AnilibriaApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val hazeState = remember { HazeState() }
    val hazeStyle = HazeMaterials.thin(MaterialTheme.colorScheme.surface)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnilibriaTopAppBar(
                modifier = Modifier
                    .hazeChild(
                        state = hazeState,
                        style = hazeStyle
                    ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            AnilibriaNavigationBar(
                modifier = Modifier
                    .hazeChild(
                        state = hazeState,
                        style = HazeMaterials.thin()
                    ),
                navController = navController
            )
        }
    ) { innerPadding ->
        AnilibriaNavGraph(
            modifier = Modifier
                .haze(state = hazeState),
            navController = navController,
            paddingValues = innerPadding
        )
    }
}

@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AnilibriaApp2(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val hazeState = remember { HazeState() }
    val hazeStyle = HazeMaterials.thin(MaterialTheme.colorScheme.surface)

    AnilibriaNavigationSuiteScaffold(
        modifier = modifier,
        topBar = {
            AnilibriaTopAppBar(
                modifier = Modifier
                    .hazeChild(
                        state = hazeState,
                        style = hazeStyle
                    )
            )
        },
        navigationSuite = { navLayoutType ->
            when (navLayoutType) {
                NavigationSuiteType.NavigationBar -> AnilibriaNavigationBar(
                    modifier = Modifier
                        .hazeChild(
                            state = hazeState,
                            style = hazeStyle
                        ),
                    navController = navController
                )

                NavigationSuiteType.NavigationRail -> AnilibriaNavigationRail(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        AnilibriaNavGraph(
            modifier = Modifier
                .haze(state = hazeState),
            navController = navController,
            paddingValues = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnilibriaTopAppBar(
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
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

private val AnilibriaTopAppBarHeight = 56.dp