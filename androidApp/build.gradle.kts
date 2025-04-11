plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.application)
    //alias(libs.plugins.baselineprofile)
}

kotlin {
    androidTarget()
    sourceSets {
        androidMain.dependencies {
            implementation(projects.shared)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.interpolator)
            implementation(libs.androidx.profileinstaller)
            implementation(libs.kotlinx.coroutines.android)
        }
    }
}

android {
    namespace = "com.xbot.anilibriarefresh"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

/*dependencies {
    implementation(projects.shared)
    baselineProfile(projects.baselineprofile)
}*/