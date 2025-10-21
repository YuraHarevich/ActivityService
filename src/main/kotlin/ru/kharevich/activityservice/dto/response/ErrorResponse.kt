package ru.kharevich.activityservice.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

/**
 *
 * @param timestamp
 * @param status
 * @param error
 * @param message
 * @param path
 */
data class ErrorResponse(

    @get:JsonProperty("timestamp") val timestamp: OffsetDateTime? = null,

    @get:JsonProperty("status") val status: Int? = null,

    @get:JsonProperty("error") val error: String? = null,

    @get:JsonProperty("message") val message: String? = null,

    @get:JsonProperty("path") val path: String? = null
) {

}