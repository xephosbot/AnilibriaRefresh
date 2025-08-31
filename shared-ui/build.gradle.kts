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
            implementation(projects.sharedUi.feature.favorite)
            implementation(projects.sharedUi.feature.home)
            implementation(projects.sharedUi.feature.player)
            implementation(projects.sharedUi.feature.profile)
            implementation(projects.sharedUi.feature.search)
            implementation(projects.sharedUi.feature.title)
            implementation(compose.foundation)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.components.resources)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.navigation.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewModel)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.core)
        }
    }
}

android {
    compileSdk = 36
    namespace = "com.xbot.sharedui"
}