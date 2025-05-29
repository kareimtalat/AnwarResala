package com.kareimt.anwarresala.viewmodels

import com.kareimt.anwarresala.data.local.CourseEntity

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val courses: List<CourseEntity> = emptyList(),
    val showAddDialog: Boolean = false
)