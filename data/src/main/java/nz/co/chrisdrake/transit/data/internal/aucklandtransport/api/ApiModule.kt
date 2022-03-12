package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import nz.co.chrisdrake.transit.data.BuildConfig
import nz.co.chrisdrake.transit.data.internal.DataTypeConverters
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

internal val apiModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) BASIC else NONE
            })
            .addInterceptor(ApiAuthorizationInterceptor())
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    single {
        Moshi.Builder()
            .add(DataTypeConverters)
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<Converter.Factory> {
        MoshiConverterFactory.create(get())
    }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("https://api.at.govt.nz/v2/")
            .client(get())
            .addConverterFactory(get())
            .build()
            .create()
    }
}
