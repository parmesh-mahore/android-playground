plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.procore.playground"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.procore.playground"
        minSdk = 24
        targetSdk = 35
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    dynamicFeatures += setOf(":profile")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Jetpack Compose BOM (Bill of Materials) - Recommended
    // This helps manage versions of different Compose libraries
    // Lifecycle (often used with Compose)
    implementation(libs.androidx.lifecycle.runtime.ktx) // instead of livedata/viewmodel ktx directly for basic runtime
    implementation(libs.androidx.lifecycle.viewmodel.compose) // For ViewModel integration with Compose

    // Activity Compose for setContent { }
    implementation(libs.androidx.activity.compose)
    implementation(project(":mylibrary"))
    implementation(project(":mylibrary2"))

    // Jetpack Compose BOM (Bill of Materials) - Recommended
    val composeBom = platform(libs.androidx.compose.bom) // Assuming you have this in libs.versions.toml
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("com.google.android.play:feature-delivery:2.1.0")
    implementation("com.google.android.play:feature-delivery-ktx:2.1.0")

    // Compose UI elements
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}