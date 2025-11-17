plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lifelog.feature.videonotes"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))

    // CameraX
    val cameraxVersion = "1.3.3"
    implementation("androidx.camera:camera-core:\$cameraxVersion")
    implementation("androidx.camera:camera-camera2:\$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:\$cameraxVersion")
    implementation("androidx.camera:camera-video:\$cameraxVersion")
    implementation("androidx.camera:camera-view:\$cameraxVersion")

    // Media3 ExoPlayer
    val media3Version = "1.3.1"
    implementation("androidx.media3:media3-exoplayer:\$media3Version")
    implementation("androidx.media3:media3-ui:\$media3Version")

    // Hilt, Compose, etc.
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
