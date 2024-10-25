plugins {
    alias(libs.plugins.xbot.android.library)
    alias(libs.plugins.xbot.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.xbot.api"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    //Retrofit dependencies
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp.brotli)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.sandwich.retrofit)
    implementation(libs.sandwich.retrofit.serialization)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    // Testing dependencies
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}