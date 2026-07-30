import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotzilla)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.shared"
        compileSdk {
            version = release(libs.versions.android.compilesdk.get().toInt())
        }
        minSdk {
            version = release(libs.versions.android.minsdk.get().toInt())
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.shared.common)
        api(projects.shared.core.domain.api)
        api(projects.shared.core.domain.impl)
        api(projects.shared.core.network.impl)
        api(projects.shared.core.data.impl)
        api(projects.shared.core.logger.api)
        api(projects.shared.core.logger.impl)
        api(projects.shared.state.home)
        api(projects.shared.state.login)
        api(projects.shared.state.player)
        api(projects.shared.state.preference)
        api(projects.shared.state.search)
        api(projects.shared.state.title)
        api(libs.koin.core)
        api(libs.koin.core.viewmodel)
        api(libs.koin.annotations)
        api(libs.kermit)
        api(libs.kermit.koin)
    }
}

kotzilla {
    versionName = "1.0.0"
}

koinCompiler {
    compileSafety = false
}
