import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.shared.domain.fixtures"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.shared.domain.api)
        implementation(projects.shared.common)
        implementation(libs.androidx.paging.core)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.arrow.core)
        implementation(libs.koin.core)
        implementation(libs.koin.annotations)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

koinCompiler {
    compileSafety = false
}
