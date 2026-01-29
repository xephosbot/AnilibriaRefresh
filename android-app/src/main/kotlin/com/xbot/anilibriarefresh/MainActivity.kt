package com.xbot.anilibriarefresh

import MainView
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.xbot.sharedapp.navigation.deeplink.ExternalUriHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            onSplashScreenExit(splashScreenViewProvider)
        }

        handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            MainView()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW) return
        if (intent.extras == null) return
        val uri = intent.data?.toString() ?: return
        ExternalUriHandler.onNewUri(uri)
    }

    private fun onSplashScreenExit(splashScreenViewProvider: SplashScreenViewProvider) {
        val accelerateInterpolator = FastOutLinearInInterpolator()
        val splashScreenView = splashScreenViewProvider.view
        val iconView = splashScreenViewProvider.iconView

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f),
            ObjectAnimator.ofFloat(iconView, View.ALPHA, 1f, 0f),
        )
        animatorSet.duration = SPLASHSCREEN_ALPHA_ANIMATION_DURATION
        animatorSet.interpolator = accelerateInterpolator

        animatorSet.doOnEnd { splashScreenViewProvider.remove() }
        animatorSet.start()
    }

    companion object {
        private const val SPLASHSCREEN_ALPHA_ANIMATION_DURATION = 200L
    }
}
