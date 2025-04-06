import androidx.compose.ui.window.ComposeUIViewController
import com.xbot.anilibriarefresh.di.uiModule
import com.xbot.anilibriarefresh.di.viewModelModule
import com.xbot.api.di.dataStoreModule
import com.xbot.api.di.networkModule
import com.xbot.data.di.repositoryModule
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.domain.di.useCaseModule
import com.xbot.home.HomeScreen
import org.koin.compose.KoinApplication

fun ViewController() = ComposeUIViewController {
    KoinApplication(
        application = {
            modules(
                networkModule,
                dataStoreModule,
                useCaseModule,
                repositoryModule,
                viewModelModule,
                uiModule
            )
        }
    ) {
        AnilibriaTheme {
            HomeScreen(
                onSearchClick = {},
                onScheduleClick = {},
                onReleaseClick = {},
            )
        }
    }
}