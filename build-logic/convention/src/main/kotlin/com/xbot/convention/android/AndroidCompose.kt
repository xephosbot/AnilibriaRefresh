/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.android

import com.android.build.api.dsl.CommonExtension
import com.xbot.convention.extensions.debugImplementation
import com.xbot.convention.extensions.getLibrary
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.getVersion
import com.xbot.convention.extensions.implementation
import com.xbot.convention.extensions.libs
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply(libs.getPlugin("compose-compiler").get().pluginId)

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.getVersion("kotlinCompiler").toString()
        }

        dependencies {
            implementation(platform(libs.getLibrary("androidx-compose-bom")))

            implementation(libs.getLibrary("androidx-compose-ui"))
            implementation(libs.getLibrary("androidx-compose-ui-util"))
            implementation(libs.getLibrary("androidx-compose-ui-tooling-preview"))

            debugImplementation(libs.getLibrary("androidx-compose-ui-tooling"))

            implementation(libs.getLibrary("androidx-compose-material"))
            implementation(libs.getLibrary("androidx-compose-material3"))
        }
    }

    // Moved without changes
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
        fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
            rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
        }.map { it.dir(dir) }

        project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

        stabilityConfigurationFile =
            rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
    }
}
