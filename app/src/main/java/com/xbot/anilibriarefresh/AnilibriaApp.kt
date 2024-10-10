package com.xbot.anilibriarefresh

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xbot.anilibriarefresh.navigation.NavGraph

@Composable
fun AnilibriaApp(modifier: Modifier = Modifier) {
    NavGraph(modifier = modifier)
}