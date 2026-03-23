import androidx.compose.runtime.Composable
import com.xbot.localization.LocaleManager
import com.xbot.localization.init
import com.xbot.sharedapp.AnilibriaApp

@Composable
fun MainView() {
    LocaleManager.init()
    AnilibriaApp()
}
