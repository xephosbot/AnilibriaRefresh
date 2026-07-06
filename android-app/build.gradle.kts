import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

val keystorePropertiesFile = project.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.xbot.anilibriarefresh"
    compileSdk {
        version = release(libs.versions.android.compilesdk.get().toInt())
    }

    defaultConfig {
        applicationId = "com.xbot.anilibriarefresh"
        minSdk {
            version = release(libs.versions.android.minsdk.get().toInt())
        }
        targetSdk {
            version = release(libs.versions.android.targetsdk.get().toInt())
        }
        versionCode = 1
        versionName = "1.0"
    }

    androidResources {
        localeFilters.addAll(arrayOf("en", "ru"))
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                keyAlias = keystoreProperties.getProperty("keyAlias") as String
                keyPassword = keystoreProperties.getProperty("keyPassword") as String
                storeFile = keystoreProperties.getProperty("storeFile")?.let { file(it) }
                storePassword = keystoreProperties.getProperty("storePassword") as String
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
        release {
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            optimization {
                enable = true
            }
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(projects.sharedUi)
    implementation(projects.sharedUi.navigation.api)
    coreLibraryDesugaring(libs.android.desugar.jdk.libs)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.interpolator)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.android)
}
