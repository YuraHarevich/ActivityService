package ru.kharevich.activityservice.service

import ru.kharevich.activityservice.dto.request.CommentRequest
import ru.kharevich.activityservice.dto.request.LikeRequest
import ru.kharevich.activityservice.dto.response.ActivityResponse
import ru.kharevich.activityservice.dto.response.CommentResponse
import ru.kharevich.activityservice.dto.response.LikeResponse
import ru.kharevich.activityservice.dto.response.PageableResponse
import java.util.UUID

interface ActivityService {
    fun getActionsByPost(postId: UUID): ActivityResponse
    fun getLikesByPostId(id: UUID, pageNumber: Int, size: Int): PageableResponse<LikeResponse>
    fun likePost(likeRequest:LikeRequest): LikeResponse
    fun getCommentsByPost(id: UUID, pageNumber: Int, size: Int): PageableResponse<CommentResponse>
    fun postComment(commentRequest: CommentRequest): CommentResponse
}