import androidx.compose.runtime.SideEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.xbot.sharedapp.di.initKoin
import window.ProvidePlatformWindowInsets
import window.enableEdgeToEdge

fun main() = application {
    initKoin {
        printLogger()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Anilibria JVM",
    ) {
        SideEffect {
            enableEdgeToEdge()
        }

        ProvidePlatformWindowInsets {
            MainView()
        }
    }
}