import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.shared.domain.fixtures"
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
        implementation(projects.shared.core.domain.api)

        implementation(libs.arrow.core)
        implementation(libs.asyncresult)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.datetime)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
