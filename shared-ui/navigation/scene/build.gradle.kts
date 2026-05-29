import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.sharedui.navigation.scene"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(libs.navigation3.runtime)
        api(libs.navigation3.ui)
        api(libs.lifecycle.viewmodel.compose)
        api(libs.declarative.snackbar.compose)
        implementation(projects.sharedUi.resource)
        implementation(libs.compose.runtime)
        implementation(libs.compose.ui)
        implementation(libs.compose.material3)
    }
}
