import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.state.login"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.shared.core.domain.api)
        api(libs.lifecycle.viewmodel)
        api(libs.lifecycle.viewmodel.savedstate)
        api(libs.orbitmvi.core)
        implementation(projects.shared.common)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.arrow.core)
    }
}
