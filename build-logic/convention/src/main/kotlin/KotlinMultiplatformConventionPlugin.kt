import com.android.build.api.AndroidPluginVersion
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.xbot.convention.configureKotlin
import com.xbot.convention.configureKotlinAndroid
import com.xbot.convention.configureKotlinJvm
import com.xbot.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.withPlugin(libs.plugins.kotlin.multiplatform.get().pluginId) {
            val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
            kmpExtension.targets.configureEach {
                when (platformType) {
                    KotlinPlatformType.jvm -> {
                        val target1 = (this as KotlinJvmTarget)
                        val target2 = (this as KotlinJvmProjectExtension)
                        val target3 = target2.target
                        println("JVM ${this::class.simpleName}")
                        val temp = listOf(target1, target2)
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
            configureKotlin<KotlinMultiplatformExtension>()
        }

        pluginManager.withPlugin(libs.plugins.kotlin.jvm.get().pluginId) {
            configureKotlinJvm()
        }

        extensions.extensionsSchema.elements.forEach { ext ->
            project.logger.lifecycle("Name: ${ext.name.padEnd(20)}, Type: ${ext.publicType}")
        }
        project.logger.lifecycle("==================================================")
    }
}
