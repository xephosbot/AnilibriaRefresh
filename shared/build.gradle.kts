import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.xbot.shared"
        compileSdk {
            version = release(libs.versions.android.compilesdk.get().toInt()) {
                minorApiLevel = 1
            }
        }
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(libs.androidx.lifecycle.viewmodel)
        api(libs.orbitmvi.core)

        implementation(projects.core.network.impl)
        implementation(projects.core.data.impl)
        implementation(projects.core.domain.impl)
        implementation(projects.shared.state.home)
        implementation(projects.shared.state.title)

        implementation(libs.kermit)
        implementation(libs.kermit.koin)
        implementation(libs.koin.annotations)
        implementation(libs.koin.core)
        implementation(libs.koin.core.viewmodel)
        implementation(libs.kotlinx.coroutines.core)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
