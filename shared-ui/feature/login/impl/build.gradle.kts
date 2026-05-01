import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.sharedui.feature.login.impl"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(projects.shared.state.login)
        implementation(projects.shared.core.domain.testFixtures)
        implementation(projects.sharedUi.common)
        implementation(projects.sharedUi.designSystem.icons)
        implementation(projects.sharedUi.designSystem.components)
        implementation(projects.sharedUi.feature.login.api)
        implementation(libs.compose.foundation)
        implementation(libs.compose.preview)
        implementation(libs.navigation3.ui)
        implementation(libs.lifecycle.viewmodel.compose)
        implementation(libs.lifecycle.runtime.compose)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.arrow.core)
        implementation(libs.orbitmvi.compose)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.koin.compose.navigation3)
        implementation(libs.koin.annotations)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

koinCompiler {
    compileSafety = false
}
