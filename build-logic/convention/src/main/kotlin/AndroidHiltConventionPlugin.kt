import com.xbot.convention.extensions.getLibrary
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.implementation
import com.xbot.convention.extensions.ksp
import com.xbot.convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.getPlugin("ksp").get().pluginId)
                apply(libs.getPlugin("hilt").get().pluginId)
            }

            dependencies {
                implementation(libs.getLibrary("hilt-android"))
                ksp(libs.getLibrary("hilt-compiler"))
            }
        }
    }
}
