import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}
dependencies {
    implementation(projects.shared)

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "md.vnastasi.atlasmemories"
    compileSdk {
        version = release(libs.versions.android.compileSdk.major.get().toInt()) {
            minorApiLevel = libs.versions.android.compileSdk.minor.get().toInt()
        }
    }

    defaultConfig {
        applicationId = "md.vnastasi.atlasmemories"
        minSdk {
            version = release(libs.versions.android.minSdk.get().toInt())
        }
        targetSdk {
            version = release(libs.versions.android.targetSdk.get().toInt())
        }
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}