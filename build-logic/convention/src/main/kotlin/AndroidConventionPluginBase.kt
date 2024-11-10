/*
 * Created by AnyGogin31 on 10.11.2024
 */

import com.xbot.convention.android.configureAndroidDetekt
import com.xbot.convention.android.configureAndroidKtlint
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class AndroidConventionPluginBase : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configurePlugin()
            configureDetekt()
            configureKtlint()
            configureAndroid()
        }
    }

    protected abstract fun Project.getPluginId(): String

    private fun Project.configurePlugin() {
        pluginManager.apply(getPluginId())
    }

    private fun Project.configureDetekt() {
        configureAndroidDetekt()
    }

    private fun Project.configureKtlint() {
        configureAndroidKtlint()
    }

    protected abstract fun Project.configureAndroid()
}
