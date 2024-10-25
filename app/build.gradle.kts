plugins {
    alias(libs.plugins.xbot.android.application)
    alias(libs.plugins.xbot.android.application.compose)
    alias(libs.plugins.xbot.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.xbot.anilibriarefresh"

    defaultConfig {
        applicationId = "com.xbot.anilibriarefresh"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    // Project-level dependencies
    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.anilibriaApi)
    baselineProfile(projects.baselineprofile)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    // AndroidX dependencies
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.material.navigation)
    implementation(libs.androidx.profileinstaller)

    // Compose dependencies
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.google.fonts)
    implementation(libs.haze.compose)
    implementation(libs.haze.compose.materials)
    implementation(libs.material.motion.compose.core)
    implementation(libs.shimmer.compose)
    implementation(libs.glide.compose)
    ksp(libs.glide.ksp)

    // Jetpack Media 3 dependencies
    implementation("androidx.media3:media3-exoplayer:1.5.0-alpha01")
    implementation("androidx.media3:media3-exoplayer-hls:1.5.0-alpha01")
    implementation("androidx.media3:media3-exoplayer-smoothstreaming:1.5.0-alpha01")
    implementation("androidx.media3:media3-common-ktx:1.5.0-alpha01")
    implementation("androidx.media3:media3-datasource-okhttp:1.5.0-alpha01")
    implementation("androidx.media3:media3-session:1.5.0-alpha01")

    // Testing dependencies
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.compose.ui.test)

    // Debug dependencies
    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}