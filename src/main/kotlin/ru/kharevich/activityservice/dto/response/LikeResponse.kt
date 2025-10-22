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

    val isLiked : Boolean

)