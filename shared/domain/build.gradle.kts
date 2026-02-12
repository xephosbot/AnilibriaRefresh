import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.xbot.shared.domain"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(libs.androidx.paging.core)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.atomicfu)
        implementation(libs.arrow.core)
        implementation(libs.arrow.coroutines)
        implementation(libs.koin.core)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}