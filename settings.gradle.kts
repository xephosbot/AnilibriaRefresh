@file:Suppress("UnstableApiUsage")

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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":domain")
include(":data")
include(":media")
include(":anilibria-api")
include(":baselineprofile")
