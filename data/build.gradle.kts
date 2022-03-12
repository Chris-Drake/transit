plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp") version Versions.ksp
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        buildConfigField("String", "AT_API_KEY", "\"${project.property("AT_API_KEY")}\"")
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    api(project(":general-transit-feed-spec"))

    implementation(Libraries.Kotlin.stdLib)
    api(Libraries.Kotlin.coroutines)

    api(Libraries.Koin.core)
    implementation(Libraries.Koin.android)

    implementation(Libraries.AndroidX.Room.core)
    implementation(Libraries.AndroidX.Room.ktx)
    ksp(Libraries.AndroidX.Room.compiler)

    implementation(Libraries.OkHttp.loggingInterceptor)
    implementation(Libraries.Retrofit.core)
    implementation(Libraries.Retrofit.moshi)
    implementation(Libraries.Moshi.core)
    implementation(Libraries.Moshi.kotlin)
}