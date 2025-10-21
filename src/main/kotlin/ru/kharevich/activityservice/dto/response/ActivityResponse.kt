package ru.kharevich.activityservice.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import java.util.UUID

/**
 *
 * @param postId Post ID
 * @param numberOfLikes Total number of likes for the post
 * @param numberOfComments Total number of comments for the post
 * @param comments List of comments (optional, might be empty)
 * @param likes List of likes (optional, might be empty)
 */
data class ActivityResponse(

    @get:JsonProperty("postId", required = true) val postId: UUID,

    @get:Min(0)
    @get:JsonProperty("numberOfLikes") val numberOfLikes: Int? = null,

    @get:Min(0)
    @get:JsonProperty("numberOfComments") val numberOfComments: Int? = null
) {

}