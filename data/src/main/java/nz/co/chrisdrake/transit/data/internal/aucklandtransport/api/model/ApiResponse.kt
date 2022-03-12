package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json

internal data class ApiResponse<T>(
    @Json(name = "status") private val status: String,
    @Json(name = "response") val data: T
)