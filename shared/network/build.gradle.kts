import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.koin.compiler)
}

// Thin wrapper — re-exports network:api + network:impl
// TODO (Phase 6): Remove Coil files from this module, then delete this wrapper module
kotlin {
    android {
        namespace = "com.xbot.shared.network"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.shared.network.api)
        api(projects.shared.network.impl)
        // Coil files still live in src/ of this module until Phase 6
        implementation(libs.coil.core)
        implementation(libs.coil.network.ktor)
        implementation(libs.ktor.client.core)
        implementation(libs.koin.core)
        implementation(libs.koin.annotations)
    }

    compilerOptions {
        freeCompilerArgs.addAll("-Xcontext-parameters", "-Xexpect-actual-classes")
    }
}

koinCompiler {
    compileSafety = false
}
