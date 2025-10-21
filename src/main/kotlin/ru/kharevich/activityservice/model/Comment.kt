package ru.kharevich.activityservice.model

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
data class Comment(
    @Id
    val id: ObjectId? = null,

    @Field("payload")
    val payload: String,

    @Field("poster_id")
    val posterId: UUID,

    @Field("post_id")
    val postId: UUID,

    @CreatedDate
    @Field("leaved_at")
    val leavedAt: LocalDateTime? = null
)