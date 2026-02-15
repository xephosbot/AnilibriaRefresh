import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.xbot.shared.network"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.encoding)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.auth)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.kotlinx.atomicfu)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.arrow.core)
        implementation(libs.koin.core)
        implementation(libs.coil.core)
        implementation(libs.coil.network.ktor)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.brotli.dec)
            implementation(libs.androidcontextprovider)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.brotli.dec)
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll("-Xcontext-parameters", "-Xexpect-actual-classes")
    }
}