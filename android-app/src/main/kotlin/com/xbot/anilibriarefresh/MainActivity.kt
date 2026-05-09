package com.xbot.anilibriarefresh

import MainView
import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.xbot.navigation.deeplink.ExternalUriHandler

class MainActivity : AppCompatActivity() {

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onNotificationPermissionResult(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            onSplashScreenExit(splashScreenViewProvider)
        }

        handleIntent(intent)
        askNotificationPermission()

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

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (hasNotificationPermission()) return
        if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) return
        requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun onNotificationPermissionResult(isGranted: Boolean) {
        when {
            isGranted -> toast(R.string.notification_permission_granted)
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) ->
                toast(R.string.notification_permission_denied)
            else -> toast(R.string.notification_permission_open_settings)
        }
    }

    private fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun toast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
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
