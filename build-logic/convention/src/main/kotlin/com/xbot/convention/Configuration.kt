/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.convention

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object Configuration {

    object Java {
        val JAVA_TARGET = JvmTarget.JVM_17
        val JAVA_VERSION = JavaVersion.VERSION_17
    }

    object Sdk {
        const val COMPILE_SDK = 35
        const val TARGET_SDK = 35
        const val MIN_SDK = 24
    }

    object App {
        const val APPLICATION_ID = "com.xbot.anilibriarefresh"
        const val VERSION_NAME = "1.0.0"
        const val VERSION_CODE = 1
    }
}
