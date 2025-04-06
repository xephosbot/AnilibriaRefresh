package com.xbot.anilibriarefresh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        //splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            //onSplashScreenExit(splashScreenViewProvider)
        //}

        enableEdgeToEdge()
        setContent {
            AnilibriaTheme {
                HomeScreen(
                    onSearchClick = {},
                    onScheduleClick = {},
                    onReleaseClick = {},
                )
            }
        }
    }
}