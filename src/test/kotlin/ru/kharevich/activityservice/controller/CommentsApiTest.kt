package ru.kharevich.activityservice.controller

import ru.kharevich.activityservice.dto.request.CommentRequest
import ru.kharevich.activityservice.dto.response.CommentResponse
import ru.kharevich.activityservice.dto.response.ErrorResponse
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class CommentsApiTest {

    private val api: CommentsApiController = CommentsApiController()

    /**
     * To test CommentsApiController.apiV1ActionsCommentGet
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun apiV1ActionsCommentGetTest() {
        val id: kotlin.String = TODO()
        val response: ResponseEntity<CommentResponse> = api.apiV1ActionsCommentGet(id)

        // TODO: test validations
    }

    /**
     * To test CommentsApiController.apiV1ActionsCommentPost
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun apiV1ActionsCommentPostTest() {
        val commentRequest: CommentRequest = TODO()
        val response: ResponseEntity<CommentResponse> = api.apiV1ActionsCommentPost(commentRequest)

        // TODO: test validations
    }
}
