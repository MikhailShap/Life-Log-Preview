pluginManagement {
    repositories {
        google()
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

rootProject.name = "LifeLogApp"
include(":app")
include(":core:ui")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":feature:today")
include(":feature:log")
include(":feature:trends")
include(":feature:meds")
include(":feature:videoNotes")
include(":feature:breathe")
include(":feature:library")
include(":feature:settings")
include(":testing")
