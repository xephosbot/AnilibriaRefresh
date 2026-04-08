import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.shared.common"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(libs.arrow.core)
        implementation(libs.orbitmvi.core)
        implementation(libs.lifecycle.viewmodel)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.atomicfu)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
