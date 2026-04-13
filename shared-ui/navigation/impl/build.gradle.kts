import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.sharedui.navigation.impl"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.sharedUi.navigation.api)
        implementation(projects.sharedUi.feature.home.api)
        implementation(projects.sharedUi.feature.favorite.api)
        implementation(projects.sharedUi.feature.preference.api)
        implementation(projects.sharedUi.feature.search.api)
        implementation(projects.sharedUi.feature.login.api)
        implementation(projects.sharedUi.feature.title.api)
        implementation(libs.navigation3.ui)
        implementation(libs.eygraber.uri)
        implementation(libs.kermit)
        implementation(libs.kotlinx.serialization.core)
    }
}
