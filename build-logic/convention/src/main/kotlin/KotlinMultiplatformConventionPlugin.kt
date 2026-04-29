import com.android.build.api.AndroidPluginVersion
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.xbot.convention.applyPlugin
import com.xbot.convention.configureKotlinAndroid
import com.xbot.convention.configureKotlinJvm
import com.xbot.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        applyPlugin(libs.plugins.kotlin.multiplatform)

        val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
        kmpExtension.targets.configureEach {
            when (platformType) {
                KotlinPlatformType.jvm -> {
                    val target = (this as KotlinJvmTarget)
                    println("JVM ${this::class.simpleName}")
                    //val temp = listOf(KotlinJvmTarget(), )
                    //configureKotlinJvm(this as KotlinJvmTarget)
                }

                KotlinPlatformType.androidJvm -> {
                    if (this is KotlinAndroidTarget) {
                        // Old kmp android integration using com.android.library or com.android.application
                        val androidExtension = extensions.getByType<CommonExtension>()
                        configureKotlinAndroid(androidExtension)
                    }
                    if (AndroidPluginVersion.getCurrent() >= AndroidPluginVersion(8, 8) &&
                        this is KotlinMultiplatformAndroidLibraryTarget
                    ) {
                        // New kmp android integration using com.android.multiplatform.library
                        configureKotlinAndroid(this)
                    }
                }

                KotlinPlatformType.native -> {
                    println("NATIVE ${this::class.simpleName}")
                }

                else -> { /*Nothing to do*/ }
            }
        }

        extensions.extensionsSchema.elements.forEach { ext ->
            project.logger.lifecycle("Name: ${ext.name.padEnd(20)}, Type: ${ext.publicType}")
        }
        project.logger.lifecycle("==================================================")
    }
}
