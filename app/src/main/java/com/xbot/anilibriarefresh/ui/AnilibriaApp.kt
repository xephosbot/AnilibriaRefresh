package com.xbot.anilibriarefresh.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.navigation.AnilibriaNavGraph
import com.xbot.anilibriarefresh.navigation.NavigationBar
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
    val hazeState = remember { HazeState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .hazeChild(
                        state = hazeState,
                        style = HazeMaterials.thin()
                    ),
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .hazeChild(
                        state = hazeState,
                        style = HazeMaterials.thin()
                    ),
                navController = navController,
                containerColor = Color.Transparent
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