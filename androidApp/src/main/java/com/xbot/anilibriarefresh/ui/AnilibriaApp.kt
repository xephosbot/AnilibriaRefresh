package com.xbot.anilibriarefresh.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.navigation.AnilibriaNavGraph
import com.xbot.anilibriarefresh.navigation.NavigationSuiteType
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationBar
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationRail
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationSuiteScaffold
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.LocalHazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun AnilibriaApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val hazeState = remember { HazeState() }
    val hazeStyle = HazeMaterials.thin(MaterialTheme.colorScheme.surface)

    AnilibriaNavigationSuiteScaffold(
        modifier = modifier,
        navigationSuite = {
            when (layoutType) {
                NavigationSuiteType.NavigationBar -> AnilibriaNavigationBar(
                    modifier = Modifier
                        .hazeChild(
                            state = hazeState,
                            style = hazeStyle,
                        ),
                    navController = navController,
                )

                NavigationSuiteType.NavigationRail -> AnilibriaNavigationRail(
                    navController = navController,
                    navContentPosition = contentPosition
                )
            }
        },
    ) {
        CompositionLocalProvider(
            LocalHazeStyle provides hazeStyle
        ) {
            AnilibriaNavGraph(
                modifier = Modifier.haze(hazeState),
                navController = navController,
            )
        }
    }
}
