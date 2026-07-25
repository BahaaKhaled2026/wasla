rootProject.name = "wasla-backend"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "wasla-api",
    "wasla-worker",
    "location-service",
    "modules:identity-tenant"
)
