import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.shared.domain)
        implementation(libs.kotlinx.datetime)
        implementation(this@kotlin.compose.ui)
        implementation(this@kotlin.compose.foundation)
        implementation(this@kotlin.compose.preview)
        implementation(this@kotlin.compose.materialIconsExtended)
        api(projects.sharedUi.resource)
        api(this@kotlin.compose.material3)
        api(this@kotlin.compose.material3AdaptiveNavigationSuite)
        api(libs.compose.ui.backhandler)
        api(libs.compose.material3.adaptive)
        api(libs.compose.material3.adaptive.layout)
        api(libs.compose.material3.adaptive.navigation)
        api(libs.navigation3.runtime)
        api(libs.navigation3.ui)
        api(libs.androidx.paging.core)
        api(libs.androidx.paging.compose)
        api(libs.lifecycle.viewmodel.compose)
        api(libs.coil.compose)
        api(libs.sticky.headers)
        api(libs.shimmer.compose)
        api(libs.material.motion.compose.core)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.uiTooling)
        }
    }
}

android {
    namespace = "com.xbot.sharedui.designsystem"
    compileSdk = libs.versions.android.compilesdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minsdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}