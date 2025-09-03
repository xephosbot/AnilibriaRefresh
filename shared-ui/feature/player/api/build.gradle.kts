plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
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
            implementation(projects.sharedUi.common)
            implementation(projects.sharedUi.designSystem)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    compileSdk = 36
    namespace = "com.xbot.sharedui.feature.player.api"
}