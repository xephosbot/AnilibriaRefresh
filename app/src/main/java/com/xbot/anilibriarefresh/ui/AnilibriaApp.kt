package com.xbot.anilibriarefresh.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.navigation.AnilibriaNavGraph
import com.xbot.anilibriarefresh.navigation.NavigationBar

@Composable
fun AnilibriaApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(navController = navController)
        }
    ) { innerPadding ->
        AnilibriaNavGraph(
            modifier = Modifier.consumeWindowInsets(innerPadding),
            navController = navController
        )
    }
}