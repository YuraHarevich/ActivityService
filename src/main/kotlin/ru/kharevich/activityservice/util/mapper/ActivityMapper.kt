package ru.kharevich.activityservice.util.mapper

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import ru.kharevich.activityservice.dto.request.CommentRequest
import ru.kharevich.activityservice.dto.request.LikeRequest
import ru.kharevich.activityservice.dto.response.ActivityResponse
import ru.kharevich.activityservice.dto.response.CommentResponse
import ru.kharevich.activityservice.dto.response.LikeResponse
import ru.kharevich.activityservice.model.Comment
import ru.kharevich.activityservice.model.Like
import java.util.UUID

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface ActivityMapper {
    fun toLikeResponse(like: Like): LikeResponse
    fun toCommentResponse(comment: Comment): CommentResponse
    fun toActivityResponse(postId: UUID, numberOfLikes: Long, numberOfComments: Long): ActivityResponse;
    fun toLike(request: LikeRequest): Like;
    fun toComment(comment: CommentRequest): Comment;
}