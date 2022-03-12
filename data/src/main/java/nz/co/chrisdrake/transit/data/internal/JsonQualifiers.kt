package nz.co.chrisdrake.transit.data.internal

import com.squareup.moshi.JsonQualifier

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class BooleanInt

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class UnixTimestamp