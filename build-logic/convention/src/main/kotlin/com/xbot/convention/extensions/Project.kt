/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Retrieves the VersionCatalog named "libs" from the project's extensions.
 *
 * @receiver The Gradle Project instance.
 * @return The VersionCatalog for the project.
 */
internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
