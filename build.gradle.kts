import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.multiplatform.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.ktorfit) apply false
    alias(libs.plugins.koin.compiler) apply false
    alias(libs.plugins.kotzilla) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

spotless {
    kotlin {
        target(sourceTree("**/src/**/*.kt"))
        ktlint(libs.versions.ktlint.get())
    }
    kotlinGradle {
        target(sourceTree("**/*.gradle.kts"))
        ktlint(libs.versions.ktlint.get())
    }
}

fun sourceTree(pattern: String) = fileTree(projectDir) {
    include(pattern)
    exclude("**/build/**", "**/.gradle/**", "**/.kotlin/**")
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(file("config/detekt/detekt.yml"))
    baseline = file("config/detekt/baseline.xml")
    basePath.set(projectDir)
    parallel = true
    source.setFrom(
        fileTree(projectDir) {
            include("**/src/**/*.kt")
            exclude("**/build/**")
        }
    )
}

subprojects {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            compilerOptions.optIn.add("kotlin.experimental.ExperimentalObjCRefinement")
        }
    }
}
