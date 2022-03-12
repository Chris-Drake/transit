plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
}

dependencies {
    api(project(":general-transit-feed-spec"))
    api(project(":data"))

    implementation(Libraries.Kotlin.stdLib)
    implementation(Libraries.Koin.core)
    implementation(Libraries.Kotlin.coroutines)
    implementation(Libraries.AndroidX.annotations)
}