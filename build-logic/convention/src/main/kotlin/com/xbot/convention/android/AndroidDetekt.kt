/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.android

import com.xbot.convention.extensions.detektPlugins
import com.xbot.convention.extensions.getLibrary
import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.getVersion
import com.xbot.convention.extensions.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidDetekt() {
    pluginManager.apply(libs.getPlugin("detekt").get().pluginId)

    extensions.configure<DetektExtension> {
        toolVersion = libs.getVersion("detekt").toString()
        parallel = true
        autoCorrect = true

        dependencies {
            detektPlugins(libs.getLibrary("detekt-formatting"))
            detektPlugins(libs.getLibrary("detekt-rules-compose"))
        }
    }
}
