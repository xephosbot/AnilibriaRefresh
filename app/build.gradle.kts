plugins {
    alias(libs.plugins.xbot.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.xbot.anilibriarefresh"
}

dependencies {
    // Project-level dependencies
    implementation(projects.shared.api)
    implementation(projects.shared.domain)
    implementation(projects.shared.data)
    implementation(projects.media)
    baselineProfile(projects.baselineprofile)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    // AndroidX dependencies
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.interpolator)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.navigation)
    implementation(libs.androidx.profileinstaller)

    // Compose dependencies
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.google.fonts)
    implementation(libs.sticky.headers)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.haze.compose)
    implementation(libs.haze.compose.materials)
    implementation(libs.material.motion.compose.core)
    implementation(libs.shimmer.compose)

    // Jetpack Media 3 dependencies
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.media3.session)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.compose.ui.test)

    // Debug dependencies
    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}