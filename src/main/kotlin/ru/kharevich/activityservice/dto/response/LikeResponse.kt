package ru.kharevich.activityservice.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import java.time.OffsetDateTime
import java.util.UUID

/**
 *
 * @param id Like ObjectId
 * @param userId User ID who liked the post
 * @param postId Post ID that was liked
 * @param likedAt When the like was created
 */
data class LikeResponse(

    @get:Pattern(regexp="^[a-fA-F0-9]{24}$")
    @get:JsonProperty("id") val id: String? = null,

    @get:JsonProperty("userId") val userId: UUID? = null,

    @get:JsonProperty("postId") val postId: UUID? = null,

    @get:JsonProperty("likedAt") val likedAt: OffsetDateTime? = null
) {

}