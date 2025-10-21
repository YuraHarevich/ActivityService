package ru.kharevich.activityservice.dto.response

data class PageableResponse<T>(
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int,
    val content: List<T>
)