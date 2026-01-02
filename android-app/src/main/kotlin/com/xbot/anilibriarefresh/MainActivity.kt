package com.xbot.anilibriarefresh

import MainView
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.PictureInPictureUiState
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.core.util.Consumer
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.xbot.player.OnPictureInPictureUiStateChangedProvider
import java.util.concurrent.CopyOnWriteArrayList

class MainActivity : ComponentActivity(), OnPictureInPictureUiStateChangedProvider {

    private val onPictureInPictureUiStateChangedListeners =
        CopyOnWriteArrayList<Consumer<PictureInPictureUiState>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            onSplashScreenExit(splashScreenViewProvider)
        }

        enableEdgeToEdge()
        setContent {
            MainView()
        }
    }

    override fun onPictureInPictureUiStateChanged(pipState: PictureInPictureUiState) {
        super.onPictureInPictureUiStateChanged(pipState)
        for (listener in onPictureInPictureUiStateChangedListeners) {
            listener.accept(pipState)
        }
    }

    override fun addOnPictureInPictureUiStateChangedListener(
        listener: Consumer<PictureInPictureUiState>
    ) {
        onPictureInPictureUiStateChangedListeners.add(listener)
    }

    override fun removeOnPictureInPictureUiStateChangedListener(
        listener: Consumer<PictureInPictureUiState>
    ) {
        onPictureInPictureUiStateChangedListeners.remove(listener)
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