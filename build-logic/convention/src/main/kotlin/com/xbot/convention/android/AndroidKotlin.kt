/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.android

import com.android.build.api.dsl.CommonExtension
import com.xbot.convention.Configuration
import com.xbot.convention.extensions.getLibrary
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

internal fun Project.configureAndroidKotlin(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply(libs.getPlugin("kotlin-android").get().pluginId)

    commonExtension.apply {
        compileSdk = Configuration.Sdk.COMPILE_SDK

        defaultConfig {
            minSdk = Configuration.Sdk.MIN_SDK
        }

        compileOptions {
            sourceCompatibility = Configuration.Java.JAVA_VERSION
            targetCompatibility = Configuration.Java.JAVA_VERSION
            isCoreLibraryDesugaringEnabled = true // Is it needed at all? The code is completely taken from nowinandroid
        }

        kotlinOptions {
            jvmTarget.set(Configuration.Java.JAVA_TARGET)
        }

        dependencies {
            add("coreLibraryDesugaring", libs.getLibrary("android-desugarJdkLibs"))
        }
    }
}

private fun Project.kotlinOptions(block: KotlinJvmCompilerOptions.() -> Unit) {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            block()
        }
    }
}
