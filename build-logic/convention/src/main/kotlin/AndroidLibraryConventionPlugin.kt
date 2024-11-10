import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.xbot.convention.Configuration
import com.xbot.convention.android.configureAndroidKotlin
import com.xbot.convention.disableUnnecessaryAndroidTests
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : AndroidConventionPluginBase() {

    override fun Project.getPluginId() = libs.getPlugin("android-library").get().pluginId

    override fun Project.configureAndroid() {
        extensions.configure<LibraryExtension> {
            configureAndroidKotlin(this)
            defaultConfig.targetSdk = Configuration.Sdk.TARGET_SDK
            defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            // The resource prefix is derived from the module name,
            // so resources inside ":core:module1" must be prefixed with "core_module1_"
            resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_").lowercase() + "_"
        }

        extensions.configure<LibraryAndroidComponentsExtension> {
            disableUnnecessaryAndroidTests(this)
        }

        dependencies {
            add("androidTestImplementation", kotlin("test"))
            add("testImplementation", kotlin("test"))

            add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
        }
    }
}
