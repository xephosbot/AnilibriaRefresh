import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.xbot.sharedui.feature.search.impl"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()

        withJava()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.shared.domain)
        implementation(projects.sharedUi.designSystem)
        implementation(projects.sharedUi.common)
        implementation(projects.sharedUi.feature.search.api)
        implementation(projects.sharedUi.feature.title.api)
        implementation(this@kotlin.compose.foundation)
        implementation(libs.navigation3.runtime)
        implementation(libs.navigation3.ui)
        implementation(libs.lifecycle.viewmodel.compose)
        implementation(libs.lifecycle.runtime.compose)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.arrow.core)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.viewmodel)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}