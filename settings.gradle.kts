rootProject.name = "AnilibriaRefresh"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":android-app")
include(":jvm-app")
include(":shared")
include(":shared:network")
include(":shared:data")
include(":shared:domain")
include(":shared-ui")
include(":shared-ui:resource")
include(":shared-ui:design-system")
include(":shared-ui:feature:common")
include(":shared-ui:feature:favorite")
include(":shared-ui:feature:home")
include(":shared-ui:feature:player")
include(":shared-ui:feature:profile")
include(":shared-ui:feature:search")
include(":shared-ui:feature:title")