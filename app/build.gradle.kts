plugins {
    id("com.android.application")
    id("kotlin-android")
}
android {
    namespace = "org.baiyu.fucksensors"
    compileSdk = 34
    defaultConfig {
        applicationId = "org.baiyu.fucksensors"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "3.1"
        resourceConfigurations += "en"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    implementation("androidx.preference:preference-ktx:1.2.1")
}