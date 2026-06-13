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
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.gradle.develocity") version ("4.2.2")
}

develocity {
    projectId.set("vnastasi/atlas-memories")

    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")

        link("VCS", "https://github.com/vnastasi/atlas-memories")

        if (System.getenv("CI")?.toBooleanStrictOrNull() == true) {
            tag("GitHub")
            value("Workflow", System.getenv("GITHUB_WORKFLOW"))
            value("Run ID", System.getenv("GITHUB_RUN_ID"))
            value("Run number", System.getenv("GITHUB_RUN_NUMBER"))
            value("Branch", System.getenv("GITHUB_HEAD_REF")?.takeUnless { it.isBlank() } ?: System.getenv("GITHUB_REF_NAME"))
            value("Commit ID", System.getenv("GITHUB_SHA"))
        } else {
            tag("Local")
        }
    }
}

rootProject.name = "AtlasMemories"

include(":androidApp")
include(":desktopApp")
include(":shared")