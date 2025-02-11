import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

val sPassword = prop.getProperty("storePassword") ?: ""
val sKeyAlias = prop.getProperty("keyAlias") ?: ""
val sKeyPassword = prop.getProperty("keyPassword") ?: ""


android {
    namespace = "com.perry.androidcommon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.perry.androidcommon"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("appDebug") {
            storeFile = file("../avatrdebug.jks")
            storePassword = sPassword
            keyAlias = sKeyAlias
            keyPassword = sKeyPassword
        }
        create("release") {
            storeFile = file("../avatrdebug.jks")
            storePassword = sPassword
            keyAlias = sKeyAlias
            keyPassword = sKeyPassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("appDebug")
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
        dataBinding = true
    }
}

dependencies {
    implementation(project(":baselib"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}