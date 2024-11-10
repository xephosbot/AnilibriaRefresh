/*
 * Created by AnyGogin31 on 10.11.2024
 */

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class AndroidConventionPluginBase : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configurePlugin()
            configureAndroid()
        }
    }

    protected abstract fun Project.getPluginId(): String

    private fun Project.configurePlugin() {
        pluginManager.apply(getPluginId())
    }

    protected abstract fun Project.configureAndroid()
}
