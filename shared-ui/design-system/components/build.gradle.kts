import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.sharedui.designsystem.components"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.sharedUi.formatters)
        api(libs.compose.material3)
        api(libs.compose.material3.adaptive.navigation.suite)
        api(libs.compose.material3.adaptive.navigation3)
        api(libs.coil.compose)
        api(libs.sticky.headers)
        api(libs.shimmer.compose)
        api(libs.material.motion.compose.core)
        api(libs.androidx.paging.compose)
        implementation(projects.sharedUi.designSystem.theme)
        implementation(projects.sharedUi.designSystem.icons)
        implementation(projects.shared.core.domain.testFixtures)
        implementation(libs.compose.preview)
        implementation(libs.compose.foundation)
        implementation(libs.kotlinx.datetime)
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
