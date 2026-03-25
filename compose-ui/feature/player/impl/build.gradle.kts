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
        namespace = "com.xbot.composeui.feature.player.impl"
        compileSdk {
            version = release(libs.versions.android.compilesdk.get().toInt()) {
                minorApiLevel = 1
            }
        }
        minSdk = libs.versions.android.minsdk.get().toInt()

        androidResources {
            enable = true
        }
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(projects.composeUi.feature.player.api)

        implementation(projects.composeUi.common)
        implementation(projects.composeUi.designSystem)
        implementation(projects.core.data.api)
        implementation(projects.core.domain.api)
        implementation(projects.core.domain.testFixtures)
        implementation(projects.shared.state.player)

        implementation(libs.compose.foundation)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.navigation3)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.kotlinx.datetime)
        implementation(libs.lifecycle.runtime.compose)
        implementation(libs.orbitmvi.compose)

        implementation(libs.lifecycle.viewmodel.compose)
        implementation(libs.navigation3.runtime)
        implementation(libs.navigation3.ui)
        implementation(libs.videoplayer.compose)
        implementation(libs.orbitmvi.compose)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.pip)
            implementation(libs.androidx.media3.common.ktx)
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.hls)
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
