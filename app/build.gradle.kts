plugins {
    id("com.google.gms.google-services")
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

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)

    //for coroutines generally
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //for lifecycle of coroutines
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //for using firebase with coroutines
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.kotlinx.coroutines.play.services)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.auth.ktx)


    //For Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
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