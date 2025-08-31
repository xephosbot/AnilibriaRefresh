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
            implementation(projects.shared.network)
            implementation(projects.shared.domain)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.paging.core)
            implementation(libs.androidx.dataStore.core)
            implementation(libs.androidx.dataStore.preferences)
            implementation(libs.koin.core)
            implementation(libs.arrow.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

android {
    compileSdk = 36
    namespace = "com.xbot.shared.data"
}