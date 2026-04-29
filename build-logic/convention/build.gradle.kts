import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.android.lint)
}

group = "com.xbot.convention.buildlogic"

// Configure the build-logic plugins to target JDK 21
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.kotlin.ksp.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    lintChecks(libs.androidx.lint.gradle)
    // The line below allow us to access the libs from version catalog directly in plugins
    gradle.serviceOf<DependenciesAccessors>().classes.asFiles.forEach {
        compileOnly(files(it.absolutePath))
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "anilibria.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("kotlinMultiplatform") {
            id = libs.plugins.anilibria.kotlin.multiplatform.get().pluginId
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
    }
}
