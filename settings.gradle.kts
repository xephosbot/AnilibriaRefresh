@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AnilibriaRefresh"

include(":androidApp")
include(":media")
include(":baselineprofile")
include(":core:designsystem")
include(":core:common")
include(":shared:domain")
include(":shared:api")
include(":shared:data")
include(":shared:together")
include(":feature:profile")
include(":feature:player")
include(":feature:favorite")
include(":feature:title")
include(":feature:search")
include(":feature:home")