import androidx.compose.ui.window.ComposeUIViewController
import com.xbot.navigation.TopLevelRoutes
import com.xbot.sharedapp.AnilibertyApp
import com.xbot.sharedapp.di.initKoin
import com.xbot.sharedapp.ios.AnilibertyTabBarController
import com.xbot.sharedapp.ios.IosNavigationChromeHost
import org.koin.mp.KoinPlatform
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    if (KoinPlatform.getKoinOrNull() == null) {
        initKoin()
    }
    val chromeHost = IosNavigationChromeHost()
    val composeViewController = ComposeUIViewController {
        AnilibertyApp(chromeHost = chromeHost)
    }
    return AnilibertyTabBarController().apply {
        configure(
            chromeHost = chromeHost,
            composeViewController = composeViewController,
            topLevelRoutes = TopLevelRoutes.toList(),
        )
    }
}
