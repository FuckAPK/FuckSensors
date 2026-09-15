plugins {
    id("fuck.android.application")
    id("fuck.compose")
    id("fuck.xposed.legacy")
}

android {
    namespace = "org.lyaaz.fucksensors"
}

dependencies {
    implementation(project(":ui"))
    implementation(libs.material)
}
