package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api

import nz.co.chrisdrake.transit.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal class ApiAuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("Ocp-Apim-Subscription-Key", BuildConfig.AT_API_KEY)
            .build()

        return chain.proceed(newRequest)
    }
}