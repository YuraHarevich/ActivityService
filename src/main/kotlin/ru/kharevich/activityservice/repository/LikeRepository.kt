package ru.kharevich.activityservice.repository

import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import ru.kharevich.activityservice.dto.response.LikeResponse
import ru.kharevich.activityservice.model.Like
import java.util.Optional
import java.util.UUID

interface LikeRepository : MongoRepository<Like, ObjectId> {
    fun findByPostId(id: UUID, pageable : Pageable): Page<LikeResponse>
    fun countByPostId(id: UUID): Long
    fun findByPostIdAndUserId(postId: UUID, userId: UUID): Optional<Like>
}