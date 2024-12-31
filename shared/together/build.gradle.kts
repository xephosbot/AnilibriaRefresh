import co.touchlab.skie.configuration.EnumInterop
import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuspendInterop

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nativecoroutines)
    alias(libs.plugins.skie)
}

kotlin {
    androidLibrary {
        namespace = "com.xbot.together"
        compileSdk = 35
        minSdk = 24
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
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        commonMain.dependencies {
            api(projects.shared.api)
            api(projects.shared.data)
            api(projects.shared.domain)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

skie {
    features {
        group {
            SealedInterop.Enabled(true)
            EnumInterop.Enabled(true)
            coroutinesInterop.set(false)
            SuspendInterop.Enabled(false)
            FlowInterop.Enabled(false)
        }
    }
}