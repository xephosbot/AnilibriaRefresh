/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.android

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureAndroidLibrary(
    libraryExtension: LibraryExtension,
) {
    libraryExtension.apply {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFile("consumer-rules.pro")
        }

        resourcePrefix = path.split("""\W""".toRegex())
            .drop(1)
            .distinct()
            .joinToString(separator = "_")
            .lowercase() + "_"
    }

    configureAndroidLibraryComponents()
}

private fun Project.configureAndroidLibraryComponents() {
    extensions.configure<LibraryAndroidComponentsExtension> {
        disableUnnecessaryAndroidTests(this)
    }
}
