import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.skie)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries {
            framework {
                export(projects.shared.api)
                export(projects.shared.data)
                export(projects.shared.domain)

                binaryOption("bundleId", "com.xbot.anilibriaframework")
                binaryOption("bundleVersion", "1")

                baseName = "AnilibriaFramework"
                isStatic = true
            }
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(projects.shared.api)
            api(projects.shared.data)
            api(projects.shared.domain)
            api(libs.koin.core)
        }
    }
}

android {
    namespace = "com.xbot.together"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
