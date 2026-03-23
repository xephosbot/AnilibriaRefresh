import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.coreui.designsystem"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.core.domain)
        implementation(projects.core.fixtures)
        implementation(libs.kotlinx.datetime)
        implementation(libs.compose.ui)
        implementation(libs.compose.foundation)
        implementation(libs.materialKolor)
        api(projects.composeUi.resource)
        api(libs.compose.preview)
        api(libs.compose.material3)
        api(libs.compose.material3.adaptive.navigation.suite)
        api(libs.compose.material3.adaptive.navigation3)
        api(libs.navigation3.runtime)
        api(libs.navigation3.ui)
        api(libs.androidx.paging.core)
        api(libs.androidx.paging.compose)
        api(libs.lifecycle.viewmodel.compose)
        api(libs.lifecycle.runtime.compose)
        api(libs.coil.compose)
        api(libs.sticky.headers)
        api(libs.shimmer.compose)
        api(libs.material.motion.compose.core)
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
