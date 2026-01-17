import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.xbot.sharedui"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    cocoapods {
        version = "1.0"
        summary = "Shared UI module with CMP for AnilibriaRefresh app"
        homepage = "https://github.com/xephosbot/AnilibriaRefresh"

        name = "SharedUI"
        podfile = project.file("../ios-app/Podfile")

        framework {
            baseName = "SharedUI"
            isStatic = true
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.shared)
        implementation(projects.sharedUi.designSystem)
        implementation(projects.sharedUi.common)
        implementation(projects.sharedUi.feature.favorite.api)
        implementation(projects.sharedUi.feature.favorite.impl)
        implementation(projects.sharedUi.feature.home.api)
        implementation(projects.sharedUi.feature.home.impl)
        implementation(projects.sharedUi.feature.player.api)
        implementation(projects.sharedUi.feature.player.impl)
        implementation(projects.sharedUi.feature.preference.api)
        implementation(projects.sharedUi.feature.preference.impl)
        implementation(projects.sharedUi.feature.search.api)
        implementation(projects.sharedUi.feature.search.impl)
        implementation(projects.sharedUi.feature.title.api)
        implementation(projects.sharedUi.feature.title.impl)
        implementation(projects.sharedUi.feature.login.api)
        implementation(projects.sharedUi.feature.login.impl)
        implementation(libs.compose.foundation)
        implementation(libs.navigation3.runtime)
        implementation(libs.navigation3.ui)
        implementation(libs.lifecycle.viewmodel.compose)
        implementation(libs.lifecycle.viewmodel.navigation3)
        implementation(libs.lifecycle.runtime.compose)
        implementation(libs.ktor.client.core)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor)
        implementation(libs.koin.core)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.koin.compose.navigation3)
    }
}
