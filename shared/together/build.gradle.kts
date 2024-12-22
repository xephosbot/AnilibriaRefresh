plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
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
        commonMain.dependencies {
            api(projects.shared.api)
            api(projects.shared.data)
            api(projects.shared.domain)
            implementation(libs.koin.core)
        }
    }
}