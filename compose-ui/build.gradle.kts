import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    //alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.composeui"
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

/*    cocoapods {
        version = "1.0"
        summary = "Shared UI module with CMP for AnilibriaRefresh app"
        homepage = "https://github.com/xephosbot/AnilibriaRefresh"

        name = "composeUi"
        podfile = project.file("../ios-app/Podfile")

        framework {
            baseName = "composeUi"
            isStatic = true
        }
    }*/

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.composeUi.common)
        implementation(projects.composeUi.designSystem)
        implementation(projects.composeUi.feature.favorite.api)
        implementation(projects.composeUi.feature.favorite.impl)
        implementation(projects.composeUi.feature.home.api)
        implementation(projects.composeUi.feature.home.impl)
        implementation(projects.composeUi.feature.login.api)
        implementation(projects.composeUi.feature.login.impl)
        implementation(projects.composeUi.feature.player.api)
        implementation(projects.composeUi.feature.player.impl)
        implementation(projects.composeUi.feature.preference.api)
        implementation(projects.composeUi.feature.preference.impl)
        implementation(projects.composeUi.feature.search.api)
        implementation(projects.composeUi.feature.search.impl)
        implementation(projects.composeUi.feature.title.api)
        implementation(projects.composeUi.feature.title.impl)
        implementation(projects.core.domain.api)
        implementation(projects.shared)

        implementation(libs.compose.foundation)
        implementation(libs.eygraber.uri)
        implementation(libs.koin.annotations)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.navigation3)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.koin.core)
        implementation(libs.lifecycle.runtime.compose)
        implementation(libs.lifecycle.viewmodel.compose)
        implementation(libs.lifecycle.viewmodel.navigation3)
        implementation(libs.navigation3.runtime)
        implementation(libs.navigation3.ui)
    }
}

koinCompiler {
    compileSafety = false
}
