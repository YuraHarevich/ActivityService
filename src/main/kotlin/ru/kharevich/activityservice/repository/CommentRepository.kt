package ru.kharevich.activityservice.repository

import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import ru.kharevich.activityservice.dto.response.CommentResponse
import ru.kharevich.activityservice.model.Comment
import java.util.UUID

interface CommentRepository : MongoRepository<Comment, ObjectId>{
    fun findByPostId(postId: UUID, pageable : Pageable): Page<CommentResponse>
    fun countByPostId(postId: UUID): Long
}