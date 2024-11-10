/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.extensions

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Adds an implementation dependency on the specified project.
 *
 * @param project The project to add as an implementation dependency.
 */
internal fun DependencyHandlerScope.implementation(project: Project) {
    "implementation"(project)
}

/**
 * Adds an implementation dependency on the specified provider.
 *
 * @param provider The provider to add as an implementation dependency.
 */
internal fun DependencyHandlerScope.implementation(provider: Provider<*>) {
    "implementation"(provider)
}

/**
 * Adds a debug implementation dependency on the specified provider.
 *
 * @param provider The provider to add as a debug implementation dependency.
 */
internal fun DependencyHandlerScope.debugImplementation(provider: Provider<*>) {
    "debugImplementation"(provider)
}

/**
 * Adds a release implementation dependency on the specified provider.
 *
 * @param provider The provider to add as a release implementation dependency.
 */
internal fun DependencyHandlerScope.releaseImplementation(provider: Provider<*>) {
    "releaseImplementation"(provider)
}

/**
 * Adds an androidTest implementation dependency on the specified provider.
 *
 * @param provider The provider to add as an androidTest implementation dependency.
 */
internal fun DependencyHandlerScope.androidTestImplementation(provider: Provider<*>) {
    "androidTestImplementation"(provider)
}

/**
 * Adds a test implementation dependency on the specified provider.
 *
 * @param provider The provider to add as a test implementation dependency.
 */
internal fun DependencyHandlerScope.testImplementation(provider: Provider<*>) {
    "testImplementation"(provider)
}

/**
 * Adds a Detekt plugin dependency on the specified provider.
 *
 * @param provider The provider that contains the Detekt plugin dependency to be added.
 */
internal fun DependencyHandlerScope.detektPlugins(provider: Provider<*>) {
    "detektPlugins"(provider)
}

/**
 * Adds a ksp dependency on the specified provider.
 *
 * @param provider The provider that contains the ksp dependency to be added.
 */
internal fun DependencyHandlerScope.ksp(provider: Provider<*>) {
    "ksp"(provider)
}
