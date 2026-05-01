import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.xbot.sharedui.designsystem.theme"
        compileSdk = libs.versions.android.compilesdk.get().toInt()
        minSdk = libs.versions.android.minsdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    applyDefaultHierarchyTemplate()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(libs.compose.material3)
        implementation(projects.sharedUi.resource)
        implementation(libs.compose.ui)
        implementation(libs.materialKolor)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(nonAndroidMain)
        }

        iosMain {
            dependsOn(nonAndroidMain)
        }
    }
}
