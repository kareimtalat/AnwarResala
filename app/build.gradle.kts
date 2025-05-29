plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // For the parcelize .. for making instances for the the course cards and course details activity
    id("org.jetbrains.kotlin.plugin.parcelize")

    // For the Room KSP
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.kareimt.anwarresala"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kareimt.anwarresala"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7") // REMOVED
    implementation(libs.androidx.lifecycle.viewmodel.compose) // ADDED
    //implementation("io.coil-kt:coil-compose:2.5.0") // REMOVED
    implementation(libs.coil.compose) // ADDED

    //for coroutines generally
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // REMOVED
    implementation(libs.kotlinx.coroutines.core) // ADDED
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // REMOVED
    implementation(libs.kotlinx.coroutines.android) // ADDED

    //for lifecycle of coroutines
    //implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7") // REMOVED
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ADDED
    //implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-alpha01") // REMOVED
    implementation(libs.androidx.lifecycle.runtime.ktx) // ADDED

    //for using firebase with coroutines
    //implementation("com.google.firebase:firebase-firestore-ktx:25.1.4") // REMOVED
    implementation(libs.firebase.firestore.ktx) // ADDED
    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3") // REMOVED
    implementation(libs.kotlinx.coroutines.play.services) // ADDED

    // Import the Firebase BoM
    //implementation(platform("com.google.firebase:firebase-bom:33.13.0")) // REMOVED
    implementation(platform(libs.firebase.bom)) // ADDED

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // implementation("com.google.firebase:firebase-analytics") // REMOVED
    implementation(libs.firebase.analytics)

    //For Retrofit
    //implementation("com.squareup.retrofit2:retrofit:2.9.0") // REMOVED
    implementation(libs.retrofit) // ADDED
    //implementation("com.squareup.retrofit2:converter-gson:2.9.0") // REMOVED
    implementation(libs.retrofit.converter.gson) // ADDED
    // Extended set of Material icons
    implementation (libs.androidx.material.icons.extended)

    // For solving the dropdownBox expanding problem
    implementation(libs.material3)
    implementation(libs.androidx.foundation)

    // For the splash screen
    implementation(libs.androidx.core.splashscreen)

    // Room import
    implementation (libs.androidx.room.runtime)
    // For annotation processing (Kotlin)
    ksp (libs.androidx.room.compiler)
    // Room KTX import
    implementation(libs.androidx.room.ktx)

    // For permissions handling
    implementation(libs.accompanist.permissions)

    // For Coil image loading from device storage or network
    implementation(libs.coil.compose)


    // For Navigation between screens
    implementation(libs.androidx.navigation.compose)
    }