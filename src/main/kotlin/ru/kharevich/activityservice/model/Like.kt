package ru.kharevich.activityservice.model

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.UUID
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
data class Like(
    @Id
    val id:ObjectId? = null,

    @Field("user_id")
    val userId: UUID,

    @Field("post_id")
    val postId: UUID,

    @CreatedDate
    @Field("liked_at")
    val likedAt: LocalDateTime? = null
)
