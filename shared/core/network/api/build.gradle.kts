import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.xbot.shared.network.api"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.shared.core.domain.api)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.auth)
        implementation(libs.arrow.core)
    }

    compilerOptions {
        freeCompilerArgs.addAll("-Xcontext-parameters", "-Xexpect-actual-classes")
    }
}
