/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.android

import com.xbot.convention.extensions.getPlugin
import com.xbot.convention.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

internal fun Project.configureAndroidKtlint() {
    pluginManager.apply(libs.getPlugin("ktlint").get().pluginId)

    extensions.configure<KtlintExtension> {
        android.set(true)
        verbose.set(true)
        outputToConsole.set(true)
        additionalEditorconfig.set(
            mapOf(
                "ktlint_standard_no-wildcard-imports" to "disabled",
                "ktlint_standard_filename" to "disabled",
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_function-signature" to "disabled",
                "ktlint_standard_class-naming" to "disabled",
                "ktlint_standard_annotation" to "disabled",
                "ktlint_standard_blank-line-before-declaration" to "disabled",
                "ktlint_standard_string-template-indent" to "disabled",
                "ktlint_standard_multiline-expression-wrapping" to "disabled"
            )
        )
        reporters {
            reporter(ReporterType.PLAIN)
        }
    }
}
