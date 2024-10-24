package com.xbot.anilibriarefresh.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextEnding(modifier: Modifier = Modifier, episodes: Int, str: String = "") {
    Text(
        text = when {
            episodes % 10 == 1 -> "$episodes эпизод" + str
            episodes % 100 in 11..14 -> "$episodes эпизодов" + str
            episodes % 10 in 2..4 -> "$episodes эпизода" + str
            else -> "$episodes эпизодов" + str
        }
    )
}