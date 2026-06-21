import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatformLibrary)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

kotlin {
    jvm()

    android {
        namespace = "md.vnastasi.atlasmemories.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }

        androidResources {
            enable = true
        }

        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.kotlinx.datetime)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.assertk)
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.compose.uiToolingPreview)
            }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(libs.assertk)
                implementation(libs.junit.jupiter.api)
                runtimeOnly(libs.junit.jupiter.engine)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.commons.imaging)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.assertk)
                implementation(libs.junit.jupiter.api)
                runtimeOnly(libs.junit.jupiter.engine)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<Detekt>().configureEach {
    multiPlatformEnabled = true
    source = layout.projectDirectory.asFileTree.matching {
        include("src/commonMain/kotlin/**")
        include("src/jvmMain/kotlin/**")
        include("src/androidMain/kotlin/**")
    }
}
