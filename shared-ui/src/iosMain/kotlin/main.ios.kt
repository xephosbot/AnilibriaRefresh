import androidx.compose.ui.window.ComposeUIViewController
import com.xbot.sharedapp.AnilibertyApp
import com.xbot.sharedapp.di.initKoin
import org.koin.mp.KoinPlatform
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    if (KoinPlatform.getKoinOrNull() == null) {
        initKoin()
    }
    return ComposeUIViewController {
        AnilibertyApp()
    }
}
