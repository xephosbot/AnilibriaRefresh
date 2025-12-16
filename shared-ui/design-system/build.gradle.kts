import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
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
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()

        withJava()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    applyDefaultHierarchyTemplate()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.shared.domain)
        implementation(libs.kotlinx.datetime)
        implementation(libs.compose.ui)
        implementation(libs.compose.foundation)
        implementation(libs.compose.preview)
        implementation(libs.materialKolor)
        api(projects.sharedUi.resource)
        api(libs.compose.material3)
        api(libs.compose.material3.adaptive.navigation.suite)
        api(libs.compose.material3.adaptive.navigation3)
        api(libs.navigation3.runtime)
        api(libs.navigation3.ui)
        api(libs.androidx.paging.core)
        api(libs.androidx.paging.compose)
        api(libs.lifecycle.viewmodel.compose)
        api(libs.coil.compose)
        api(libs.sticky.headers)
        api(libs.shimmer.compose)
        api(libs.kmpalette.core)
        api(libs.material.motion.compose.core)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
        }
        val nonAndroid by creating {
            dependsOn(commonMain.get())
        }
        jvmMain.get().dependsOn(nonAndroid)
        iosMain.get().dependsOn(nonAndroid)
    }
}
