import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.shared"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.shared.di)
        api(projects.shared.common)
        api(projects.shared.core.domain.api)
        api(projects.shared.state.home)
        api(projects.shared.state.login)
        api(projects.shared.state.player)
        api(projects.shared.state.preference)
        api(projects.shared.state.search)
        api(projects.shared.state.title)
    }
}
