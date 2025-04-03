plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.xbot.compose)
}

android {
    namespace = "com.xbot.title"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    // Project-level dependencies
    api(projects.shared.api)
    api(projects.shared.domain)
    api(projects.core.designsystem)
    api(projects.core.common)

    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Koin dependencies
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    // AndroidX dependencies
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.navigation.compose)

    // Compose dependencies
    implementation(compose.material3)
    implementation(compose.material3AdaptiveNavigationSuite)
    implementation(compose.materialIconsExtended)
    implementation(compose.preview)
    implementation(libs.shimmer.compose)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(compose.uiTest)

    // Debug dependencies
    debugImplementation(compose.testManifest)
    debugImplementation(compose.uiTooling)
}