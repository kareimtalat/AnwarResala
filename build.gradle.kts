// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //for the percelize
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.0.21" apply false

    // For the Room KSP
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

    // for firebase
    id("com.google.gms.google-services") version "4.4.2" apply false
}