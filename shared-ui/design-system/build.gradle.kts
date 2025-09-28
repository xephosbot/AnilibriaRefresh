import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.xbot.sharedui.designsystem"
        compileSdk = 36
        minSdk = 24

        withJava()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.domain)
            api(projects.sharedUi.resource)
            implementation(libs.kotlinx.datetime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            api(compose.material3)
            api(compose.material3AdaptiveNavigationSuite)
            api(libs.compose.ui.backhandler)
            api(libs.compose.material3.adaptive)
            api(libs.compose.material3.adaptive.layout)
            api(libs.compose.material3.adaptive.navigation)
            api(libs.androidx.paging.core)
            api(libs.androidx.paging.compose)
            api(libs.coil.compose)
            api(libs.sticky.headers)
            api(libs.shimmer.compose)
            api(libs.material.motion.compose.core)
        }
    }
}