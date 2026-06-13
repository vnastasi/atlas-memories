import kotlinx.kover.gradle.plugin.dsl.AggregationType

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.multiplatformLibrary) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

kover {
    reports {
        total {
            verify {
                rule {
                    disabled = true // Enable once proper tests are added
                    bound {
                        aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                        minValue = 90
                    }
                }
            }
        }
    }
}

dependencies {
    kover(projects.shared)
    kover(projects.androidApp)
    kover(projects.desktopApp)
}
