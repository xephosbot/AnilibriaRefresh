package com.xbot.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    configureKotlin<KotlinAndroidProjectExtension>()
}

internal fun Project.configureKotlinAndroid(
    multiplatformExtension: KotlinMultiplatformAndroidLibraryExtension,
) {
    configureKotlin<KotlinMultiplatformExtension>()
}

internal fun Project.configureKotlinJvm(
    jvmExtension: JavaPluginExtension
) {
    configureKotlin<KotlinJvmProjectExtension>()
}

private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    val warningsAsErrors = providers.gradleProperty("warningsAsErrors").map {
        it.toBoolean()
    }.orElse(false)

    (this as? HasConfigurableKotlinCompilerOptions<*>)?.compilerOptions {
        allWarningsAsErrors = warningsAsErrors
        if (this is KotlinJvmCompilerOptions) {
            jvmTarget = JvmTarget.JVM_21
        }

        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xcontext-parameters",
            "-Xexpect-actual-classes",
            "-Xconsistent-data-class-copy-visibility",
        )
    }
}
