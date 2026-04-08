import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.sharedui.formatters"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.sharedUi.resource)
        implementation(projects.shared.core.domain.api)
        implementation(libs.compose.runtime)
        implementation(libs.compose.ui)
        implementation(libs.kotlinx.datetime)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }
    }
}
