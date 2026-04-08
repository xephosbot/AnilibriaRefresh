import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.shared.di"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.shared.core.network.impl)
        implementation(projects.shared.core.domain.impl)
        implementation(projects.shared.core.data.impl)
        implementation(projects.shared.state.home)
        implementation(projects.shared.state.login)
        implementation(projects.shared.state.player)
        implementation(projects.shared.state.preference)
        implementation(projects.shared.state.search)
        implementation(projects.shared.state.title)
        implementation(libs.koin.core)
        implementation(libs.koin.core.viewmodel)
        implementation(libs.koin.annotations)
        implementation(libs.kermit)
        implementation(libs.kermit.koin)
    }
}

koinCompiler {
    compileSafety = false
}
