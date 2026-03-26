import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.shared.domain.impl"
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

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.shared.core.common)
        implementation(projects.shared.core.data.api)
        implementation(projects.shared.core.domain.api)

        implementation(libs.androidx.paging.core)
        implementation(libs.arrow.core)
        implementation(libs.arrow.coroutines)
        implementation(libs.asyncresult)
        implementation(libs.koin.annotations)
        implementation(libs.koin.core)
        implementation(libs.kotlinx.atomicfu)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.datetime)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

koinCompiler {
    compileSafety = false
}
