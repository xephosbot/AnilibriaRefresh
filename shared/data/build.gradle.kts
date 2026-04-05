import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.shared.data"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        // Thin wrapper — re-exports data:impl
        // TODO (Phase 6): Remove CoilCacheDirProvider files, then delete this wrapper module
        api(projects.shared.data.impl)
        implementation(projects.shared.network)
        // CoilCacheDirProvider files still live in src/ of this module until Phase 6
        implementation(libs.coil.core)
        implementation(libs.coil.network.ktor)
        implementation(libs.koin.core)
        implementation(libs.koin.annotations)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

koinCompiler {
    compileSafety = false
}
