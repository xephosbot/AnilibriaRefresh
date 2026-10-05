import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.xbot.sharedapp.AnilibertyApp
import com.xbot.sharedapp.di.initKoin
import com.xbot.sharedapp.ios.AnilibertyTabBarController
import com.xbot.sharedapp.ios.NativeTabBarChrome
import com.xbot.sharedapp.navigation.LocalNavigationChrome
import org.koin.mp.KoinPlatform
import platform.UIKit.UIViewController

// Called from Swift as Main_iosKt.MainViewController().
@Suppress("ktlint:standard:function-naming")
fun MainViewController(): UIViewController {
    if (KoinPlatform.getKoinOrNull() == null) {
        initKoin()
    }
    val tabBarController = AnilibertyTabBarController()
    val chrome = NativeTabBarChrome(tabBarController)
    val composeViewController = ComposeUIViewController {
        CompositionLocalProvider(LocalNavigationChrome provides chrome) {
            AnilibertyApp()
        }
    }
    tabBarController.attach(composeViewController)
    return tabBarController
}
