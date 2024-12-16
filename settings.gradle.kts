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
include(":media")
include(":baselineprofile")
include(":core:designsystem")
include(":shared:domain")
include(":shared:api")
include(":shared:data")
include(":shared:common")
