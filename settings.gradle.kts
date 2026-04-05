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
        mavenLocal()
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
include(":shared:common")
include(":shared:network")
include(":shared:network:api")
include(":shared:network:impl")
include(":shared:data")
include(":shared:data:api")
include(":shared:data:impl")
include(":shared:data:test-fixtures")
include(":shared:domain")
include(":shared:domain:api")
include(":shared:domain:impl")
include(":shared:domain:test-fixtures")
include(":shared:state:home")
include(":shared:state:search")
include(":shared:state:title")
include(":shared:state:player")
include(":shared:state:preference")
include(":shared:state:login")
include(":shared-ui")
include(":shared-ui:resource")
include(":shared-ui:formatters")
include(":shared-ui:navigation:api")
include(":shared-ui:navigation:impl")
include(":shared-ui:design-system")
include(":shared-ui:common")
include(":shared-ui:feature:favorite:api")
include(":shared-ui:feature:favorite:impl")
include(":shared-ui:feature:home:api")
include(":shared-ui:feature:home:impl")
include(":shared-ui:feature:player:api")
include(":shared-ui:feature:player:impl")
include(":shared-ui:feature:preference:api")
include(":shared-ui:feature:preference:impl")
include(":shared-ui:feature:search:api")
include(":shared-ui:feature:search:impl")
include(":shared-ui:feature:title:api")
include(":shared-ui:feature:title:impl")
include(":shared-ui:feature:login:api")
include(":shared-ui:feature:login:impl")
