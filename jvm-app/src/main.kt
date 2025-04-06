import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.map.Mapper
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.anilibriarefresh.di.uiModule
import com.xbot.anilibriarefresh.di.viewModelModule
import com.xbot.api.di.dataStoreModule
import com.xbot.api.di.networkModule
import com.xbot.data.di.repositoryModule
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.domain.di.useCaseModule
import com.xbot.domain.models.Poster
import com.xbot.home.HomeScreen
import org.koin.compose.KoinApplication

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .components {
                    add(Mapper<Poster, String> { data, _ -> data.src })
                }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(context)
                        .build()
                }
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
        }
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
}