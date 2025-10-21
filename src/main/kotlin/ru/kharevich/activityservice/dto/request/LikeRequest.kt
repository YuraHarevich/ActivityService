package ru.kharevich.activityservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

/**
 *
 * @param userId User ID who is liking the post
 * @param postId Post ID being liked
 */
data class LikeRequest(

    @get:JsonProperty("userId", required = true) val userId: UUID,

    @get:JsonProperty("postId", required = true) val postId: UUID
) {

}