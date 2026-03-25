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
include(":core:common")
include(":core:network:api")
include(":core:network:impl")
include(":core:data:api")
include(":core:data:impl")
include(":core:data:test-fixtures")
include(":core:domain:api")
include(":core:domain:impl")
include(":core:domain:test-fixtures")
include(":shared")
include(":shared:state:home")
include(":shared:state:title")
include(":shared:state:player")
include(":compose-ui")
include(":compose-ui:resource")
include(":compose-ui:design-system")
include(":compose-ui:common")
include(":compose-ui:feature:favorite:api")
include(":compose-ui:feature:favorite:impl")
include(":compose-ui:feature:home:api")
include(":compose-ui:feature:home:impl")
include(":compose-ui:feature:player:api")
include(":compose-ui:feature:player:impl")
include(":compose-ui:feature:preference:api")
include(":compose-ui:feature:preference:impl")
include(":compose-ui:feature:search:api")
include(":compose-ui:feature:search:impl")
include(":compose-ui:feature:title:api")
include(":compose-ui:feature:title:impl")
include(":compose-ui:feature:login:api")
include(":compose-ui:feature:login:impl")
