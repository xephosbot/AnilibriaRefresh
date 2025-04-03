package com.xbot.convention.compose

import ComposePlugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

abstract class ComposeExtension(
    project: Project
): ExtensionAware {
    val dependencies = ComposePlugin.Dependencies(project)
}