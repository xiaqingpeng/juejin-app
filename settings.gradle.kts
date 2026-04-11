rootProject.name = "Juejin"
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
    }
}

dependencyResolutionManagement {
    repositories {

        maven("https://maven.aliyun.com")
        maven("https://maven.aliyun.com")

        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        // JetBrains Compose repository for all Compose Multiplatform dependencies
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }
        // Kotlin repository for Kotlin Multiplatform dependencies
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

// 共享模块 - 核心
include(":shared:core:common")
include(":shared:core:storage")
include(":shared:core:network")

// 共享模块 - 领域
include(":shared:domain")

// 共享模块 - UI
include(":shared:ui:theme")
include(":shared:ui:components")