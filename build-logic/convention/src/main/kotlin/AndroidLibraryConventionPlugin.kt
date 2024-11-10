import com.android.build.gradle.LibraryExtension
import com.xbot.convention.android.configureAndroidKotlin
import com.xbot.convention.android.configureAndroidLibrary
import com.xbot.convention.extensions.getLibrary
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.implementation
import com.xbot.convention.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : AndroidConventionPluginBase() {

    override fun Project.getPluginId() = libs.getPlugin("android-library").get().pluginId

    override fun Project.configureAndroid() {
        extensions.configure<LibraryExtension> {
            configureAndroidLibrary(this)
            configureAndroidKotlin(this)
        }

        dependencies {
//            androidTestImplementation(libs.getLibrary("kotlin-test"))
//            testImplementation(libs.getLibrary("kotlin-test")

            implementation(libs.getLibrary("androidx-tracing-ktx"))
        }
    }
}
