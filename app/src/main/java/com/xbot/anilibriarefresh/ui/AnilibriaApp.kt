package com.xbot.anilibriarefresh.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.navigation.AnilibriaNavGraph
import com.xbot.anilibriarefresh.navigation.AnilibriaNavigationSuiteScaffold
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationBar
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationRail
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
                    navContentPosition = contentPosition
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