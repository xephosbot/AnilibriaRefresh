import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.skie)
    alias(libs.plugins.kotzilla)
}

kotlin {
    android {
        namespace = "com.xbot.shared"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(projects.shared.di)
            export(projects.shared.common)
            export(projects.shared.core.domain.api)
            export(projects.shared.state.home)
            export(projects.shared.state.login)
            export(projects.shared.state.player)
            export(projects.shared.state.preference)
            export(projects.shared.state.search)
            export(projects.shared.state.title)
            export(libs.lifecycle.runtime)
            export(libs.lifecycle.viewmodel)
            export(libs.lifecycle.viewmodel.savedstate)
            export(libs.koin.core)
        }
    }

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
        api(libs.lifecycle.runtime)
        api(libs.lifecycle.viewmodel)
        api(libs.lifecycle.viewmodel.savedstate)
        api(libs.koin.core)
    }
}

kotzilla {
    versionName = "1.0.0"
}
