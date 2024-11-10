import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.xbot.convention.extensions.getLibrary
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.implementation
import com.xbot.convention.extensions.ksp
import com.xbot.convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.getPlugin("room").get().pluginId)
                apply(libs.getPlugin("ksp").get().pluginId)
            }

            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

            extensions.configure<RoomExtension> {
                // The schemas directory contains a schema file for each version of the Room database.
                // This is required to enable Room auto migrations.
                // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                implementation(libs.getLibrary("room-runtime"))
                implementation(libs.getLibrary("room-ktx"))
                ksp(libs.getLibrary("room-compiler"))
            }
        }
    }
}
