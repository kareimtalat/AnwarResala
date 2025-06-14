package com.kareimt.anwarresala.viewmodels

import com.kareimt.anwarresala.data.local.course.CourseEntity

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val courses: List<CourseEntity> = emptyList(),
)