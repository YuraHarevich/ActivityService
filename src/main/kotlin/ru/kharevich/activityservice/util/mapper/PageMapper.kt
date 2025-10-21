package ru.kharevich.activityservice.util.mapper

import org.mapstruct.Mapper
import org.mapstruct.InjectionStrategy
import org.springframework.data.domain.Page
import ru.kharevich.activityservice.dto.response.PageableResponse

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface PageMapper {

    fun <T> toResponse(page: Page<T>): PageableResponse<T> {
        return PageableResponse(
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            currentPage = page.number,
            pageSize = page.size,
            content = page.content
        )
    }
}