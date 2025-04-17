plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.ktor)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewModel)
            implementation(libs.androidx.dataStore.core)
            implementation(libs.androidx.dataStore.preferences)
            implementation(libs.androidx.paging.core)
            implementation(libs.androidx.paging.compose)
            implementation(compose.foundation)
            //implementation(compose.material3)
            implementation("org.jetbrains.compose.material3:material3:1.8.0-alpha03")
            implementation("org.jetbrains.compose.material3:material3-adaptive-navigation-suite:1.8.0-alpha03")
            implementation("org.jetbrains.compose.material3:material3-window-size-class:1.8.0-beta02")
            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.1.0-beta02")
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.9.10+dev2347")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
            implementation(libs.sticky.headers)
            implementation(libs.shimmer.compose)
            implementation(libs.material.motion.compose.core)
            implementation(libs.coil.compose)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.brotli.dec)
            implementation(libs.koin.android)
            implementation(libs.androidx.paging.android)
            implementation(libs.coil.network.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.coil.network.ktor)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.brotli.dec)
            implementation(libs.coil.network.ktor)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.xbot.shared.resources"
    generateResClass = always
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.xbot.common"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
