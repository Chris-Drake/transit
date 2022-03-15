import org.codehaus.groovy.runtime.ProcessGroovyMethods.execute
import org.codehaus.groovy.runtime.ProcessGroovyMethods.getText

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp") version Versions.ksp
}

val gitCommitHash: String by lazy {
    val process = execute("git rev-parse --short HEAD")
    getText(process).trim()
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "nz.co.chrisdrake.transit"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = 1
        versionName = "0.1"

        buildConfigField("String", "GIT_SHA", "\"$gitCommitHash\"")

        manifestPlaceholders["googleMapsKey"] = project.property("AT_GOOGLE_MAPS_KEY") as String
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.compiler
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Libraries.Kotlin.stdLib)
    implementation(Libraries.Kotlin.reflect)
    implementation(Libraries.Kotlin.coroutines)
    implementation(Libraries.AndroidX.appCompat)
    implementation(Libraries.AndroidX.core)
    implementation(Libraries.AndroidX.design)
    implementation(Libraries.AndroidX.Navigation.ui)
    implementation(Libraries.AndroidX.Navigation.fragment)
    implementation(Libraries.AndroidX.Lifecycle.lifecycles)
    implementation(Libraries.AndroidX.Compose.activity)
    implementation(Libraries.AndroidX.Compose.material)
    implementation(Libraries.AndroidX.Compose.runtime)
    implementation(Libraries.AndroidX.Compose.tooling)
    implementation(Libraries.AndroidX.Compose.Accompanist.insets)
    implementation(Libraries.AndroidX.Compose.Accompanist.systemUiController)
    implementation(Libraries.PlayServices.maps)
    implementation(Libraries.mapsCompose)

    implementation(platform(Libraries.Firebase.bom))
    implementation(Libraries.Firebase.analytics)
    implementation(Libraries.Firebase.crashlytics)

    implementation(Libraries.Koin.core)
    implementation(Libraries.Koin.android)
}
