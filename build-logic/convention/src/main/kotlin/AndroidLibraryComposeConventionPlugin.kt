import com.android.build.gradle.LibraryExtension
import com.xbot.convention.android.configureAndroidCompose
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin : AndroidConventionPluginBase() {

    override fun Project.getPluginId() = libs.getPlugin("xbot-android-library").get().pluginId

    override fun Project.configureAndroid() {
        extensions.configure<LibraryExtension> {
            configureAndroidCompose(this)
        }
    }
}