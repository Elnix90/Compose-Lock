plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.vanniktech.maven.publish)
}

kotlin {
    explicitApi()
}

android {
    namespace = "io.github.elnix90.lock"

    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    packaging {
        jniLibs.keepDebugSymbols.add("**/*.so")
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Integration with activities
    implementation(libs.androidx.activity.compose)
    // Compose Material Design
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material3.window.size.class1)
    // Animations
    implementation(libs.androidx.compose.animation)
    // Tooling support (Previews, etc.)
    implementation(libs.androidx.compose.ui.tooling)
    // Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // UI Tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.customview.poolingcontainer)
}
