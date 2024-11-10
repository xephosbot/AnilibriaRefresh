/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.extensions

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

/**
 * Retrieves a plugin from the VersionCatalog by its name.
 *
 * @param pluginName The name of the plugin to find.
 * @return A Provider containing the PluginDependency.
 * @throws NoSuchElementException If the plugin is not found in the catalog.
 */
internal fun VersionCatalog.getPlugin(pluginName: String): Provider<PluginDependency> {
    return findPlugin(pluginName).orElseThrow {
        NoSuchElementException("Plugin with name $pluginName not found in the catalog")
    }
}

/**
 * Retrieves a bundle from the VersionCatalog by its name.
 *
 * @param bundleName The name of the bundle to find.
 * @return A Provider containing the ExternalModuleDependencyBundle.
 * @throws NoSuchElementException If the bundle is not found in the catalog.
 */
internal fun VersionCatalog.getBundle(bundleName: String): Provider<ExternalModuleDependencyBundle> {
    return findBundle(bundleName).orElseThrow {
        NoSuchElementException("Bundle with name $bundleName not found in the catalog")
    }
}

/**
 * Retrieves a library from the VersionCatalog by its name.
 *
 * @param libraryName The name of the library to find.
 * @return A Provider containing the MinimalExternalModuleDependency.
 * @throws NoSuchElementException If the library is not found in the catalog.
 */
internal fun VersionCatalog.getLibrary(libraryName: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(libraryName).orElseThrow {
        NoSuchElementException("Library with name $libraryName not found in the catalog")
    }
}

/**
 * Retrieves a version from the VersionCatalog by its name.
 *
 * @param versionName The name of the version to find.
 * @return The VersionConstraint for the specified version.
 * @throws NoSuchElementException If the version is not found in the catalog.
 */
internal fun VersionCatalog.getVersion(versionName: String): VersionConstraint {
    return findVersion(versionName).orElseThrow {
        NoSuchElementException("Version with name $versionName not found in the catalog")
    }
}
