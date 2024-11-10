plugins {
    alias(libs.plugins.xbot.android.library.compose)
    alias(libs.plugins.xbot.android.hilt)
}

android {
    namespace = "com.xbot.media"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Compose dependencies
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Jetpack Media 3 dependencies
    implementation("androidx.media3:media3-exoplayer:1.5.0-alpha01")
    implementation("androidx.media3:media3-exoplayer-hls:1.5.0-alpha01")
    implementation("androidx.media3:media3-common-ktx:1.5.0-alpha01")
    implementation("androidx.media3:media3-session:1.5.0-alpha01")

    // Testing dependencies
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}
