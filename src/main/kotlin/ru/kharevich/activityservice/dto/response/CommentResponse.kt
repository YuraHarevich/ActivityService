package ru.kharevich.activityservice.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.Setter
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.UUID

/**
 *
 * @param id Comment ObjectId
 * @param payload Comment content
 * @param posterId User ID who posted the comment
 * @param postId Post ID that was commented on
 * @param leavedAt When the comment was created
 */

data class CommentResponse(
    @get:Pattern(regexp="^[a-fA-F0-9]{24}$")
    var id: ObjectId? = null,
    var payload: String? = null,
    var posterId: UUID? = null,
    var postId: UUID? = null,
    var leavedAt: LocalDateTime? = null
)