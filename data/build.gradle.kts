plugins {
    alias(libs.plugins.xbot.android.library)
    alias(libs.plugins.xbot.android.hilt)
    alias(libs.plugins.xbot.android.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.xbot.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Project-level dependencies
    implementation(projects.anilibriaApi)
    implementation(projects.domain)

    implementation(libs.sandwich.retrofit)
    implementation(libs.sandwich.retrofit.serialization)

    // AndroidX dependencies
    implementation(libs.androidx.dataStore)
    implementation(libs.androidx.paging.runtime)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    // Testing dependencies
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}