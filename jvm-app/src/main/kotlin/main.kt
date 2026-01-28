import androidx.compose.runtime.SideEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.xbot.sharedapp.di.initKoin
import com.xbot.sharedapp.navigation.deeplink.ExternalUriHandler
import java.awt.Desktop
import window.ProvidePlatformWindowInsets
import window.enableEdgeToEdge

fun main(args: Array<String>) {
    if (System.getProperty("os.name").contains("Mac")) {
        Desktop.getDesktop().setOpenURIHandler { event ->
            ExternalUriHandler.onNewUri(event.uri.toString())
        }
    } else {
        ExternalUriHandler.onNewUri(args.getOrNull(0).toString())
    }

    application {
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
}
