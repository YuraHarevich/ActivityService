package ru.kharevich.activityservice.controller

import ru.kharevich.activityservice.dto.response.ActivityResponse
import ru.kharevich.activityservice.dto.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import lombok.AllArgsConstructor
import lombok.RequiredArgsConstructor
import ru.kharevich.activityservice.service.ActivityService

import kotlin.collections.List
import kotlin.collections.Map

@RestController
@RequestMapping
class ActivitiesApiController(
    private var activityService: ActivityService
) {

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/api/v1/activities/posts/{id}"],
        produces = ["application/json"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun apiV1ActionsPostIdGet( @PathVariable("id") id: java.util.UUID): ActivityResponse {
        return activityService.getActionsByPost(id);
    }
}
