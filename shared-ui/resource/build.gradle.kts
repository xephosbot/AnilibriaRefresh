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
            implementation(libs.kotlinx.datetime)
            implementation(compose.foundation)
            api(compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.xbot.resources"
    generateResClass = auto
}

android {
    compileSdk = 36
    namespace = "com.xbot.sharedui.resource"
}