plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nativecoroutines)
}

kotlin {
    androidLibrary {
        namespace = "com.xbot.domain"
        compileSdk = 35
        minSdk = 24
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.paging.core)
            implementation(libs.koin.core)
        }
    }
}
