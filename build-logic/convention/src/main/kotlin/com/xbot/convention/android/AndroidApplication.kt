/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention.android

import com.android.build.api.dsl.ApplicationExtension
import com.xbot.convention.Configuration
import org.gradle.api.Project

internal fun Project.configureAndroidApplication(
    applicationExtension: ApplicationExtension,
) {
    applicationExtension.apply {
        defaultConfig {
            applicationId = Configuration.App.APPLICATION_ID

            targetSdk = Configuration.Sdk.TARGET_SDK

            versionName = Configuration.App.VERSION_NAME
            versionCode = Configuration.App.VERSION_CODE

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile(
                        "proguard-android-optimize.txt"
                    ),
                    "proguard-rules.pro"
                )
                signingConfig = signingConfigs.getByName("debug")
            }
        }

        packaging {
            resources {
                pickFirsts.add("META-INF/INDEX.LIST")
            }
        }
    }
}
