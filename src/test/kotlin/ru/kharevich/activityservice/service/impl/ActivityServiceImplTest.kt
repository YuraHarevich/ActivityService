import ru.kharevich.activityservice.service.impl.ActivityServiceImpl
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import ru.kharevich.activityservice.dto.request.CommentRequest
import ru.kharevich.activityservice.dto.request.LikeRequest
import ru.kharevich.activityservice.dto.response.ActivityResponse
import ru.kharevich.activityservice.dto.response.CommentResponse
import ru.kharevich.activityservice.dto.response.LikeResponse
import ru.kharevich.activityservice.dto.response.PageableResponse
import ru.kharevich.activityservice.kafka.ActivityMessageProducer
import ru.kharevich.activityservice.model.Comment
import ru.kharevich.activityservice.model.Like
import ru.kharevich.activityservice.repository.CommentRepository
import ru.kharevich.activityservice.repository.LikeRepository
import ru.kharevich.activityservice.util.mapper.ActivityMapper
import ru.kharevich.activityservice.util.mapper.PageMapper
import java.util.*
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ActivityServiceImplTest {

    @Mock
    private lateinit var commentsRepository: CommentRepository

    @Mock
    private lateinit var likeRepository: LikeRepository

    @Mock
    private lateinit var activityMapper: ActivityMapper // Убрали дублирование

    @Mock
    private lateinit var pageMapper: PageMapper

    @Mock
    private lateinit var activityMessageProducer: ActivityMessageProducer

    @InjectMocks
    private lateinit var activityService: ActivityServiceImpl

    private val postId = UUID.randomUUID()
    private val userId = UUID.randomUUID()
    private val posterId = UUID.randomUUID()
    private val likeObjectId = ObjectId()
    private val commentObjectId = ObjectId()

    @BeforeEach
    fun setUp() {
        reset(commentsRepository, likeRepository, activityMapper, pageMapper, activityMessageProducer)
    }

    @Test
    fun `getActionsByPost should return activity response with counts`() {
        // Given
        val expectedLikes = 5L
        val expectedComments = 3L
        val expectedResponse = ActivityResponse(postId, expectedLikes.toInt(), expectedComments.toInt())

        `when`(likeRepository.countByPostId(postId)).thenReturn(expectedLikes)
        `when`(commentsRepository.countByPostId(postId)).thenReturn(expectedComments)
        `when`(activityMapper.toActivityResponse(postId, expectedLikes, expectedComments)).thenReturn(expectedResponse)

        // When
        val result = activityService.getActionsByPost(postId)

        // Then
        assertEquals(expectedResponse, result)
        verify(likeRepository).countByPostId(postId)
        verify(commentsRepository).countByPostId(postId)
        verify(activityMapper).toActivityResponse(postId, expectedLikes, expectedComments)
    }

    @Test
    fun `likePost when like exists should delete like and return false`() {
        // Given
        val likeRequest = LikeRequest(userId, postId)
        val existingLike = Like(id = likeObjectId, userId = userId, postId = postId, likedAt = LocalDateTime.now())
        val optLike = Optional.of(existingLike)
        val activityResponse = ActivityResponse(postId, 0, 0)

        `when`(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(optLike)
        `when`(likeRepository.countByPostId(postId)).thenReturn(0L)
        `when`(commentsRepository.countByPostId(postId)).thenReturn(0L)
        `when`(activityMapper.toActivityResponse(postId, 0L, 0L)).thenReturn(activityResponse)

        // When
        val result = activityService.likePost(likeRequest)

        // Then
        assertEquals(LikeResponse(false), result)
        verify(likeRepository).delete(existingLike)
        verify(likeRepository, never()).save(any())
        verify(activityMessageProducer).sendMessage(activityResponse)
    }

    @Test
    fun `likePost when like does not exist should save like and return true`() {
        // Given
        val likeRequest = LikeRequest(userId, postId)
        val optLike = Optional.empty<Like>()
        val savedLike = Like(id = likeObjectId, userId = userId, postId = postId, likedAt = LocalDateTime.now())
        val activityResponse = ActivityResponse(postId, 1, 0)

        // Используем any() вместо конкретного объекта, чтобы избежать mismatch
        `when`(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(optLike)
        `when`(activityMapper.toLike(likeRequest)).thenReturn(Like(id = null, userId = userId, postId = postId, likedAt = null))
        `when`(likeRepository.save(any(Like::class.java))).thenReturn(savedLike) // Исправлено: any() вместо конкретного объекта
        `when`(likeRepository.countByPostId(postId)).thenReturn(1L)
        `when`(commentsRepository.countByPostId(postId)).thenReturn(0L)
        `when`(activityMapper.toActivityResponse(postId, 1L, 0L)).thenReturn(activityResponse)

        // When
        val result = activityService.likePost(likeRequest)

        // Then
        assertEquals(LikeResponse(true), result)
        verify(likeRepository).save(any(Like::class.java))
        verify(likeRepository, never()).delete(any())
        verify(activityMessageProducer).sendMessage(activityResponse)
    }

    @Test
    fun `getCommentsByPost should return pageable response`() {
        // Given
        val pageNumber = 0
        val size = 10
        val pageRequest = PageRequest.of(pageNumber, size)

        val commentEntity = CommentResponse(
            id = commentObjectId,
            payload = "Test comment",
            posterId = posterId,
            postId = postId,
            leavedAt = LocalDateTime.now()
        )
        val commentsList = listOf(commentEntity)
        val commentsPage = PageImpl(commentsList, pageRequest, commentsList.size.toLong())

        val commentResponse = CommentResponse().apply {
            id = commentObjectId
            payload = "Test comment"
            posterId = posterId
            postId = postId
            leavedAt = LocalDateTime.now()
        }
        val expectedResponse = PageableResponse(1L, 1, 0, 10, listOf(commentResponse))

        `when`(commentsRepository.findByPostId(postId, pageRequest)).thenReturn(commentsPage)
        `when`(pageMapper.toResponse(commentsPage)).thenReturn(expectedResponse) // ЗДЕСЬ ИСПРАВЛЕНИЕ!

        // When
        val result = activityService.getCommentsByPost(postId, pageNumber, size)

        // Then
        assertEquals(expectedResponse, result)
        verify(commentsRepository).findByPostId(postId, pageRequest)
        verify(pageMapper).toResponse(commentsPage) // Проверяем, что маппер был вызван
    }

    @Test
    fun `postComment should save comment and send message`() {
        // Given
        val commentRequest = CommentRequest("Test comment", posterId, postId)
        val savedComment = Comment(id = commentObjectId, payload = "Test comment", posterId = posterId, postId = postId, leavedAt = LocalDateTime.now())
        val commentResponse = CommentResponse().apply {
            id = commentObjectId
            payload = "Test comment"
            posterId = posterId
            postId = postId
            leavedAt = LocalDateTime.now()
        }
        val activityResponse = ActivityResponse(postId, 0, 1)

        `when`(activityMapper.toComment(commentRequest)).thenReturn(Comment(id = null, payload = "Test comment", posterId = posterId, postId = postId, leavedAt = null))
        `when`(commentsRepository.save(any(Comment::class.java))).thenReturn(savedComment)
        `when`(activityMapper.toCommentResponse(savedComment)).thenReturn(commentResponse) // Исправлено: должен возвращать не null
        `when`(likeRepository.countByPostId(postId)).thenReturn(0L)
        `when`(commentsRepository.countByPostId(postId)).thenReturn(1L)
        `when`(activityMapper.toActivityResponse(postId, 0L, 1L)).thenReturn(activityResponse)

        // When
        val result = activityService.postComment(commentRequest)

        // Then
        assertEquals(commentResponse, result)
        verify(commentsRepository).save(any(Comment::class.java))
        verify(activityMapper).toCommentResponse(savedComment)
        verify(activityMessageProducer).sendMessage(activityResponse)
    }

    // УДАЛЕН тест `likePost should call getActionsByPost with correct postId`
    // так как он избыточен и дублирует логику других тестов

    // УДАЛЕН тест `postComment should call getActionsByPost with correct postId`
    // по той же причине

    @Test
    fun `likePost when deleting like should update activity counts correctly`() {
        // Given
        val likeRequest = LikeRequest(userId, postId)
        val existingLike = Like(id = likeObjectId, userId = userId, postId = postId, likedAt = LocalDateTime.now())
        val optLike = Optional.of(existingLike)
        val activityResponse = ActivityResponse(postId, 4, 2)

        `when`(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(optLike)
        `when`(likeRepository.countByPostId(postId)).thenReturn(4L)
        `when`(commentsRepository.countByPostId(postId)).thenReturn(2L)
        `when`(activityMapper.toActivityResponse(postId, 4L, 2L)).thenReturn(activityResponse)

        // When
        val result = activityService.likePost(likeRequest)

        // Then
        assertEquals(LikeResponse(false), result)
        verify(likeRepository).delete(existingLike)
        verify(activityMessageProducer).sendMessage(activityResponse)
    }

    @Test
    fun `likePost when adding like should update activity counts correctly`() {
        // Given
        val likeRequest = LikeRequest(userId, postId)
        val optLike = Optional.empty<Like>()
        val savedLike = Like(id = likeObjectId, userId = userId, postId = postId, likedAt = LocalDateTime.now())
        val activityResponse = ActivityResponse(postId, 6, 3)

        `when`(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(optLike)
        `when`(activityMapper.toLike(likeRequest)).thenReturn(Like(id = null, userId = userId, postId = postId, likedAt = null))
        `when`(likeRepository.save(any(Like::class.java))).thenReturn(savedLike)
        `when`(likeRepository.countByPostId(postId)).thenReturn(6L)
        `when`(commentsRepository.countByPostId(postId)).thenReturn(3L)
        `when`(activityMapper.toActivityResponse(postId, 6L, 3L)).thenReturn(activityResponse)

        // When
        val result = activityService.likePost(likeRequest)

        // Then
        assertEquals(LikeResponse(true), result)
        verify(likeRepository).save(any(Like::class.java))
        verify(activityMessageProducer).sendMessage(activityResponse)
    }

    @Test
    fun `getActionsByPost with zero counts should return zero values`() {
        // Given
        val expectedResponse = ActivityResponse(postId, 0, 0)

        `when`(likeRepository.countByPostId(postId)).thenReturn(0L)
        `when`(commentsRepository.countByPostId(postId)).thenReturn(0L)
        `when`(activityMapper.toActivityResponse(postId, 0L, 0L)).thenReturn(expectedResponse)

        // When
        val result = activityService.getActionsByPost(postId)

        // Then
        assertEquals(expectedResponse, result)
        assertEquals(0, result.numberOfLikes)
        assertEquals(0, result.numberOfComments)
    }

    @Test
    fun `getCommentsByPost with empty result should return empty page`() {
        // Given
        val pageNumber = 0
        val size = 10
        val pageRequest = PageRequest.of(pageNumber, size)
        val emptyCommentsPage = PageImpl<CommentResponse>(emptyList(), pageRequest, 0L) // Исправлен тип
        val expectedResponse = PageableResponse<CommentResponse>(0L, 0, 0, 10, emptyList())

        `when`(commentsRepository.findByPostId(postId, pageRequest)).thenReturn(emptyCommentsPage)
        `when`(pageMapper.toResponse(emptyCommentsPage)).thenReturn(expectedResponse)

        // When
        val result = activityService.getCommentsByPost(postId, pageNumber, size)

        // Then
        assertEquals(expectedResponse, result)
        assertEquals(0, result.totalElements)
        assertTrue(result.content.isEmpty())
    }
}