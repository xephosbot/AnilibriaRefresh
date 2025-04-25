import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.xbot.shared.di.initKoin

fun main() = application {
    initKoin {
        printLogger()
    }

    Window(::exitApplication) {
        MainView()
    }
}