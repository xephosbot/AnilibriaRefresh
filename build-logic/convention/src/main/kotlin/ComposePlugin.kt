import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.xbot.convention.android.configureAndroidCompose
import com.xbot.convention.compose.ComposeExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ComposePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies.extensions.add("compose", Dependencies(project))
            extensions.create("compose", ComposeExtension::class.java, project)

            val isApplication = plugins.hasPlugin("com.android.application")
            val isLibrary = plugins.hasPlugin("com.android.library")

            when {
                isApplication -> {
                    extensions.configure<ApplicationExtension> {
                        configureAndroidCompose(this)
                    }
                }
                isLibrary -> {
                    extensions.configure<LibraryExtension> {
                        configureAndroidCompose(this)
                    }
                }
                else -> {
                    logger.warn("ComposePlugin is applied to a non-Android module. Compose configuration will be skipped.")
                }
            }
        }
    }

    class Dependencies(project: Project) {
        val adaptive get() = composeDependency("androidx.compose.material3.adaptive:adaptive")
        val adaptiveLayout get() = composeDependency("androidx.compose.material3.adaptive:adaptive-layout")
        val animation get() = composeDependency("androidx.compose.animation:animation")
        val animationGraphics get() = composeDependency("androidx.compose.animation:animation-graphics")
        val foundation get() = composeDependency("androidx.compose.foundation:foundation")
        val material get() = composeDependency("androidx.compose.material:material")
        val materialNavigation get() = composeDependency("androidx.compose.material:material-navigation")
        val material3 get() = composeDependency("androidx.compose.material3:material3")
        val material3AdaptiveNavigationSuite get() = composeDependency("androidx.compose.material3:material3-adaptive-navigation-suite")
        val runtime get() = composeDependency("androidx.compose.runtime:runtime")
        val ui get() = composeDependency("androidx.compose.ui:ui")
        val uiTest get() = composeDependency("androidx.compose.ui:ui-test-junit4")
        val uiTooling get() = composeDependency("androidx.compose.ui:ui-tooling")
        val uiUtil get() = composeDependency("androidx.compose.ui:ui-util")
        val testManifest get() = composeDependency("androidx.compose.ui:ui-test-manifest")
        val preview get() = composeDependency("androidx.compose.ui:ui-tooling-preview")
        val materialIconsExtended get() = composeDependency("androidx.compose.material:material-icons-extended")
        val googleFonts get() = composeDependency("androidx.compose.ui:ui-text-google-fonts")
    }
}

private fun composeDependency(groupWithArtifact: String) = groupWithArtifact