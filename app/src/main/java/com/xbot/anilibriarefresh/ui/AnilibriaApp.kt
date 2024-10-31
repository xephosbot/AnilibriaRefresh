package com.xbot.anilibriarefresh.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.navigation.AnilibriaNavGraph
import com.xbot.anilibriarefresh.navigation.NavigationSuiteType
import com.xbot.anilibriarefresh.navigation.Route
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationBar
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationRail
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationSuiteScaffold
import com.xbot.anilibriarefresh.ui.components.AnilibriaTopAppBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AnilibriaApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val hazeState = remember { HazeState() }
    val hazeStyle = HazeMaterials.thin(MaterialTheme.colorScheme.surface)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    AnilibriaNavigationSuiteScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnilibriaTopAppBar(
                modifier = Modifier
                    .hazeChild(
                        state = hazeState,
                        style = hazeStyle
                    ),
                navController = navController,
                //TODO: Delete it after testing
                onNavigationClick = {
                    navController.navigate(Route.Player(9789))
                },
                scrollBehavior = scrollBehavior
            )
        },
        navigationSuite = {
            when (layoutType) {
                NavigationSuiteType.NavigationBar -> AnilibriaNavigationBar(
                    modifier = Modifier
                        .hazeChild(
                            state = hazeState,
                            style = hazeStyle
                        ),
                    navController = navController
                )

                NavigationSuiteType.NavigationRail -> AnilibriaNavigationRail(
                    navController = navController,
                    navContentPosition = contentPosition,
                    onNavigationClick = {
                        navController.navigate(Route.Player(9789))
                    }
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