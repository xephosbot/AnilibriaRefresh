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

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.domain)
            api(projects.sharedUi.resource)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            api(compose.material3)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation("org.jetbrains.compose.material3:material3-window-size-class:1.9.0-alpha03")
            api("org.jetbrains.compose.ui:ui-backhandler:1.9.0-alpha03")
            api("org.jetbrains.compose.material3.adaptive:adaptive:1.2.0-alpha03")
            api("org.jetbrains.compose.material3.adaptive:adaptive-layout:1.2.0-alpha03")
            api("org.jetbrains.compose.material3.adaptive:adaptive-navigation:1.2.0-alpha03")
            api(libs.androidx.paging.core)
            api(libs.androidx.paging.compose)
            api(libs.sticky.headers)
            api(libs.shimmer.compose)
            api(libs.coil.compose)
            api(libs.material.motion.compose.core)
        }
        androidMain.dependencies {
            implementation(compose.preview)
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

android {
    compileSdk = 36
    namespace = "com.xbot.sharedui.designsystem"

    defaultConfig {
        minSdk = 24
    }
}