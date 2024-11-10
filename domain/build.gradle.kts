plugins {
    alias(libs.plugins.xbot.android.library)
}

android {
    namespace = "com.xbot.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}
