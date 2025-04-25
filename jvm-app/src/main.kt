import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.xbot.sharedapp.di.initKoin

fun main() = application {
    initKoin {
        printLogger()
    }

    Window(onCloseRequest = ::exitApplication) {
        MainView()
    }
}