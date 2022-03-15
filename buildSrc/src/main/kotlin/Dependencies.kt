object Versions {
    val compileSdk = 31
    val targetSdk = 31
    val minSdk = 26

    val gradlePlugin = "7.1.2"
    val kotlin = "1.6.10"
    val ksp = "$kotlin-1.0.2"
    val googleServicesPlugin = "4.3.10"
    val crashlyticsPlugin = "2.8.1"
    val coroutines = "1.6.0"
    val navigation = "2.4.1"
    val lifecycle = "2.4.1"
    val koin = "3.1.5"
    val okHttp = "4.9.3"
    val retrofit = "2.9.0"
    val moshi = "1.13.0"
    val room = "2.4.1"

    object Compose {
        val accompanist = "0.23.1"
        val activity = "1.4.0"
        val compiler = "1.1.1"
        val material = "1.1.1"
        val runtime = "1.2.0-alpha03" // Compatibility with https://github.com/googlemaps/android-maps-compose/blob/main/build.gradle#L4
        val tooling = "1.1.1"
    }
}

object Libraries {
    object Kotlin {
        val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    }

    object AndroidX {
        val appCompat = "androidx.appcompat:appcompat:1.4.1"
        val core = "androidx.core:core-ktx:1.7.0"
        val design = "com.google.android.material:material:1.5.0"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.2"
        val work = "androidx.work:work-runtime-ktx:2.7.1"
        val dataStore = "androidx.datastore:datastore:1.0.0"
        val annotations = "androidx.annotation:annotation:1.3.0"

        object Navigation {
            val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
            val ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        }

        object Lifecycle {
            val lifecycles = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        }

        object Room {
            val core = "androidx.room:room-runtime:${Versions.room}"
            val compiler = "androidx.room:room-compiler:${Versions.room}"
            val ktx = "androidx.room:room-ktx:${Versions.room}"
        }

        object Compose {
            val activity = "androidx.activity:activity-compose:${Versions.Compose.activity}"
            val material = "androidx.compose.material:material:${Versions.Compose.material}"
            val tooling = "androidx.compose.ui:ui-tooling:${Versions.Compose.tooling}"
            val runtime = "androidx.compose.runtime:runtime:${Versions.Compose.runtime}"

            object Accompanist {
                val insets = "com.google.accompanist:accompanist-insets:${Versions.Compose.accompanist}"
                val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.Compose.accompanist}"
            }
        }
    }

    object PlayServices {
        val maps = "com.google.android.gms:play-services-maps:18.0.2"
    }

    val mapsCompose = "com.google.maps.android:maps-compose:1.3.1"

    object Firebase {
        val bom = "com.google.firebase:firebase-bom:29.0.4"
        val analytics = "com.google.firebase:firebase-analytics-ktx"
        val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object Koin {
        val core = "io.insert-koin:koin-core:${Versions.koin}"
        val android = "io.insert-koin:koin-android:${Versions.koin}"
    }

    object OkHttp {
        val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    }

    object Retrofit {
        val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        val moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    }

    object Moshi {
        val core = "com.squareup.moshi:moshi:${Versions.moshi}"
        val kotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    }
}