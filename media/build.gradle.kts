plugins {
    alias(libs.plugins.xbot.android.library.compose)
}

android {
    namespace = "com.xbot.media"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Koin dependencies
    implementation(platform(libs.koinBom))
    implementation(libs.koinCore)
    implementation(libs.koinAndroid)
    implementation(libs.koinAndroidCompose)

    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Compose dependencies
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Jetpack Media 3 dependencies
    implementation("androidx.media3:media3-exoplayer:1.5.0-beta01")
    implementation("androidx.media3:media3-exoplayer-hls:1.5.0-beta01")
    implementation("androidx.media3:media3-common-ktx:1.5.0-beta01")
    implementation("androidx.media3:media3-session:1.5.0-beta01")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}
