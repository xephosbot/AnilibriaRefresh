import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

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

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.sharedUi.designSystem)
            implementation(projects.sharedUi.common)
            implementation(projects.sharedUi.feature.favorite.api)
            implementation(projects.sharedUi.feature.favorite.impl)
            implementation(projects.sharedUi.feature.home.api)
            implementation(projects.sharedUi.feature.home.impl)
            implementation(projects.sharedUi.feature.player.api)
            implementation(projects.sharedUi.feature.player.impl)
            implementation(projects.sharedUi.feature.profile.api)
            implementation(projects.sharedUi.feature.profile.impl)
            implementation(projects.sharedUi.feature.search.api)
            implementation(projects.sharedUi.feature.search.impl)
            implementation(projects.sharedUi.feature.title.api)
            implementation(projects.sharedUi.feature.title.impl)
            implementation(compose.foundation)
            implementation(libs.navigation.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}

android {
    compileSdk = 36
    namespace = "com.xbot.sharedui"

    defaultConfig {
        minSdk = 24
    }
}