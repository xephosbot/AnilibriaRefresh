plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.xbot.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.xbot.anilibriarefresh"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    implementation(projects.shared.together)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.media)
    implementation(projects.feature.home)
    implementation(projects.feature.favorite)
    implementation(projects.feature.profile)
    implementation(projects.feature.search)
    implementation(projects.feature.title)
    implementation(projects.feature.player)

    baselineProfile(projects.baselineprofile)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Koin dependencies
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
    implementation(libs.androidx.profileinstaller)

    // Compose dependencies
    implementation(compose.material3)
    implementation(compose.material3AdaptiveNavigationSuite)
    implementation(compose.materialIconsExtended)
    implementation(compose.preview)
    implementation(compose.googleFonts)
    implementation(compose.materialNavigation)
    implementation(libs.sticky.headers)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.material.motion.compose.core)
    implementation(libs.shimmer.compose)

    // Jetpack Media 3 dependencies
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.media3.session)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(compose.uiTest)

    // Debug dependencies
    debugImplementation(compose.testManifest)
    debugImplementation(compose.uiTooling)
}