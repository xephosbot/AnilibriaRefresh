import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.composeui.common"
        compileSdk {
            version = release(libs.versions.android.compilesdk.get().toInt()) {
                minorApiLevel = 1
            }
        }
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.composeUi.designSystem)
        implementation(projects.shared.core.domain.api)

        implementation(libs.arrow.core)
        implementation(libs.compose.foundation)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.navigation3)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.lifecycle.runtime.compose)
        implementation(libs.lifecycle.viewmodel.compose)
        implementation(libs.lifecycle.viewmodel.navigation3)
        implementation(libs.navigation3.runtime)
        implementation(libs.navigation3.ui)
    }
}
