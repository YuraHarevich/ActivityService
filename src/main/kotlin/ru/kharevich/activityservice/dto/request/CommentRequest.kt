package ru.kharevich.activityservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size
import java.util.UUID

/**
 *
 * @param payload Comment content
 * @param posterId User ID who is posting the comment
 * @param postId Post ID being commented on
 */
data class CommentRequest(

    @get:Size(max=1000)
    @get:JsonProperty("payload", required = true) val payload: String,

    @get:JsonProperty("posterId", required = true) val posterId: UUID,

    @get:JsonProperty("postId", required = true) val postId: UUID
) {

}