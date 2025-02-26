plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.xbot.compose)
}

android {
    namespace = "com.xbot.player"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Project-level dependencies
    api(projects.shared.domain)
    api(projects.core.designsystem)
    api(projects.core.common)

    // Jetpack Media 3 dependencies
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.ui.compose)

    // Koin dependencies
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.guava)

    // AndroidX dependencies
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)

    // Compose dependencies
    implementation(compose.material3)
    implementation(compose.material3AdaptiveNavigationSuite)
    implementation(compose.materialIconsExtended)
    implementation(compose.preview)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(compose.uiTest)

    // Debug dependencies
    debugImplementation(compose.testManifest)
    debugImplementation(compose.uiTooling)
}