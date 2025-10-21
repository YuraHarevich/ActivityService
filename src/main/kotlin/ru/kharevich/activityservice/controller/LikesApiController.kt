package ru.kharevich.activityservice.controller

import ru.kharevich.activityservice.dto.response.ErrorResponse
import ru.kharevich.activityservice.dto.request.LikeRequest
import ru.kharevich.activityservice.dto.response.LikeResponse
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
import ru.kharevich.activityservice.dto.response.PageableResponse
import ru.kharevich.activityservice.service.ActivityService
import java.util.UUID

import kotlin.collections.List
import kotlin.collections.Map

@RestController
@RequestMapping()
@RequiredArgsConstructor
class LikesApiController(
    private val activityService: ActivityService
) {


    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/api/v1/activities/likes"],
        produces = ["application/json"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun apiV1ActionsLikeGet(@NotNull @Pattern(regexp="^[a-fA-F0-9]{24}$")  @Valid @RequestParam(value = "id", required = true) id: UUID,
                            @RequestParam(defaultValue = "0") @Min(0) page_number: Int,
                            @RequestParam(defaultValue = "20") size: Int): PageableResponse<LikeResponse> {
        return activityService.getLikesByPostId(id, page_number, size);
    }

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/api/v1/activities/likes"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun apiV1ActionsLikePost( @Valid @RequestBody likeRequest: LikeRequest) {
        activityService.likePost(likeRequest);
    }
}
