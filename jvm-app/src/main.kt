import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.xbot.sharedapp.di.initKoin
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() = application {
    initKoin {
        printLogger()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Anilibria JVM",
    ) {
        DevelopmentEntryPoint {
            MainView()
        }
    }
}