package com.xbot.convention

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.accessors.runtime.extensionOf
import org.gradle.kotlin.dsl.apply
import org.gradle.plugin.use.PluginDependency

val Project.libs: LibrariesForLibs
    get() = extensionOf(this, "libs") as LibrariesForLibs

fun Project.applyPlugin(name: Provider<PluginDependency>) {
    apply(plugin = name.get().pluginId)
}
