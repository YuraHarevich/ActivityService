package ru.kharevich.activityservice.service.impl

import lombok.RequiredArgsConstructor
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.kharevich.activityservice.dto.request.CommentRequest
import ru.kharevich.activityservice.dto.request.LikeRequest
import ru.kharevich.activityservice.dto.response.ActivityResponse
import ru.kharevich.activityservice.dto.response.CommentResponse
import ru.kharevich.activityservice.dto.response.LikeResponse
import ru.kharevich.activityservice.dto.response.PageableResponse
import ru.kharevich.activityservice.kafka.ActivityMessageProducer
import ru.kharevich.activityservice.repository.CommentRepository
import ru.kharevich.activityservice.repository.LikeRepository
import ru.kharevich.activityservice.service.ActivityService
import ru.kharevich.activityservice.util.mapper.ActivityMapper
import ru.kharevich.activityservice.util.mapper.PageMapper
import java.util.UUID

@Service
@RequiredArgsConstructor
class ActivityServiceImpl(
    private var commentsRepository: CommentRepository,
    private var likeRepository: LikeRepository,
    private var mapper: ActivityMapper,
    private var pageMapper: PageMapper,
    private var activityMapper: ActivityMapper,
    private var activityMessageProducer: ActivityMessageProducer
) : ActivityService {

    override fun getActionsByPost(postId: UUID): ActivityResponse {
        val numberOfLikes = likeRepository.countByPostId(postId)
        val numberOfComments = commentsRepository.countByPostId(postId)
        val activityResponse = mapper.toActivityResponse(postId, numberOfLikes, numberOfComments)
        return activityResponse
    }

    override fun getLikesByPostId(id: UUID,
                                  pageNumber: Int,
                                  size: Int): PageableResponse<LikeResponse> {
        val likes = likeRepository.findByPostId(id, PageRequest.of(pageNumber,size))
        return pageMapper.toResponse(likes)
    }

    override fun likePost(likeRequest: LikeRequest): LikeResponse {
        var optLike = likeRepository.findByPostIdAndUserId(likeRequest.postId, likeRequest.userId)
        var likeResponse = LikeResponse(true)
        when {
            optLike.isPresent -> {
                likeRepository.delete(optLike.get())
                likeResponse = LikeResponse(false)
            }
            optLike.isEmpty -> {
                likeRepository.save(activityMapper.toLike(likeRequest))
                likeResponse = LikeResponse(true)
            }
        }
        val activityResponse = getActionsByPost(likeRequest.postId)
        activityMessageProducer.sendMessage(activityResponse)
        return likeResponse
    }

    override fun getCommentsByPost(
        id: UUID,
        pageNumber: Int,
        size: Int
    ): PageableResponse<CommentResponse> {
        val comments = commentsRepository.findByPostId(id, PageRequest.of(pageNumber,size))
        return pageMapper.toResponse(comments)
    }

    override fun postComment(commentRequest: CommentRequest): CommentResponse {
        val comment = activityMapper.toComment(commentRequest)
        val savedComment = commentsRepository.save(comment)
        val commentResponse =  mapper.toCommentResponse(savedComment)
        val activityResponse = getActionsByPost(commentRequest.postId)
        activityMessageProducer.sendMessage(activityResponse)
        return commentResponse
    }

}