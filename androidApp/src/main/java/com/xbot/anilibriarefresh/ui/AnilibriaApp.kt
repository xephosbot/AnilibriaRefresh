package com.xbot.anilibriarefresh.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.navigation.AnilibriaNavGraph
import com.xbot.anilibriarefresh.navigation.NavigationSuiteType
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationBar
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationRail
import com.xbot.anilibriarefresh.ui.components.AnilibriaNavigationSuiteScaffold

@Composable
fun AnilibriaApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    AnilibriaNavigationSuiteScaffold(
        modifier = modifier,
        navigationSuite = {
            when (layoutType) {
                NavigationSuiteType.NavigationBar -> AnilibriaNavigationBar(
                    navController = navController,
                )

                NavigationSuiteType.NavigationRail -> AnilibriaNavigationRail(
                    navController = navController,
                    navContentPosition = contentPosition
                )
            }
        },
    ) {
        AnilibriaNavGraph(
            navController = navController,
        )
    }
}
