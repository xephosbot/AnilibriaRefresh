import androidx.compose.runtime.Composable
import com.xbot.common.state.AppState
import com.xbot.sharedapp.AnilibertyApp
import com.xbot.sharedapp.rememberAnilibertyAppState

@Composable
fun MainView(appState: AppState = rememberAnilibertyAppState()) {
    AnilibertyApp(appState = appState)
}
