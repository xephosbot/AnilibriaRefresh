import com.android.build.api.dsl.ApplicationExtension
import com.xbot.convention.android.configureAndroidApplication
import com.xbot.convention.android.configureAndroidCompose
import com.xbot.convention.android.configureAndroidKotlin
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

// Merged with AndroidApplicationComposeConventionPlugin
class AndroidApplicationConventionPlugin : AndroidConventionPluginBase() {

    override fun Project.getPluginId() = libs.getPlugin("android-application").get().pluginId

    override fun Project.configureAndroid() {
        extensions.configure<ApplicationExtension> {
            configureAndroidApplication(this)
            configureAndroidCompose(this)
            configureAndroidKotlin(this)
        }
    }
}