plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(projects.shared.network)
            api(projects.shared.data)
            api(projects.shared.domain)
        }
    }
}

android {
    compileSdk = 36
    namespace = "com.xbot.shared"
}