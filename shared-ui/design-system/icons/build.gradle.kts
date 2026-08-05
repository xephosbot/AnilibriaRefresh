import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.sharedui.designsystem.icons"
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
        api(libs.compose.ui)
    }
}
