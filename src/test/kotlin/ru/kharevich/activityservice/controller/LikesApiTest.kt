package ru.kharevich.activityservice.controller

import ru.kharevich.activityservice.dto.response.ErrorResponse
import ru.kharevich.activityservice.dto.request.LikeRequest
import ru.kharevich.activityservice.dto.response.LikeResponse
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class LikesApiTest {

    private val api: LikesApiController = LikesApiController()

    /**
     * To test LikesApiController.apiV1ActionsLikeGet
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun apiV1ActionsLikeGetTest() {
        val id: kotlin.String = TODO()
        val response: ResponseEntity<LikeResponse> = api.apiV1ActionsLikeGet(id)

        // TODO: test validations
    }

    /**
     * To test LikesApiController.apiV1ActionsLikePost
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun apiV1ActionsLikePostTest() {
        val likeRequest: LikeRequest = TODO()
        val response: ResponseEntity<LikeResponse> = api.apiV1ActionsLikePost(likeRequest)

        // TODO: test validations
    }
}
