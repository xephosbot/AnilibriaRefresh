import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.composeui.designsystem"
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
        api(projects.composeUi.resource)

        api(libs.androidx.paging.compose)
        api(libs.androidx.paging.core)
        api(libs.coil.compose)
        api(libs.compose.material3)
        api(libs.compose.material3.adaptive.navigation.suite)
        api(libs.compose.material3.adaptive.navigation3)
        api(libs.compose.preview)
        api(libs.lifecycle.runtime.compose)
        api(libs.lifecycle.viewmodel.compose)
        api(libs.material.motion.compose.core)
        api(libs.navigation3.runtime)
        api(libs.navigation3.ui)
        api(libs.shimmer.compose)
        api(libs.sticky.headers)

        implementation(projects.core.domain.api)
        implementation(projects.core.domain.testFixtures)

        implementation(libs.compose.foundation)
        implementation(libs.compose.ui)
        implementation(libs.kotlinx.datetime)
        implementation(libs.materialKolor)
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
