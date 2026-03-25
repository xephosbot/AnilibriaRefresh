import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.koin.compiler)
}
kotlin {
    android {
        namespace = "com.xbot.login.state"
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

        implementation(projects.core.domain.api)

        implementation(libs.arrow.core)
        implementation(libs.compose.runtime.annotation)
        implementation(libs.koin.annotations)
        implementation(libs.koin.core)
        implementation(libs.koin.core.viewmodel)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.orbitmvi.viewmodel)
    }
}
