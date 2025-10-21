package ru.kharevich.activityservice.controller

import ru.kharevich.activityservice.dto.response.ActivityResponse
import ru.kharevich.activityservice.dto.response.ErrorResponse
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class ActivitiesApiTest {

    private val api: ActivitiesApiController = ActivitiesApiController()

    /**
     * To test ActivitiesApiController.apiV1ActionsPostIdGet
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun apiV1ActionsPostIdGetTest() {
        val id: java.util.UUID = TODO()
        val response: ResponseEntity<ActivityResponse> = api.apiV1ActionsPostIdGet(id)

        // TODO: test validations
    }
}
