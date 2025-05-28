package com.kareimt.anwarresala.viewmodels

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.Course
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kareimt.anwarresala.data.local.BranchEntity
import com.kareimt.anwarresala.data.local.CourseEntity
import com.kareimt.anwarresala.data.local.DatabaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CoursesViewModel(context:Context) : ViewModel() {
    private val _branches = MutableStateFlow<List<BranchEntity>>(emptyList())
    val branches: StateFlow<List<BranchEntity>> = _branches.asStateFlow()

    private val _courses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val courses: StateFlow<List<CourseEntity>> = _courses

    private val courseDao = DatabaseProvider.getDatabase(context).courseDao()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch{
            _courses.value = courseDao.getAllCourses()
        }
    }

    fun addCourse(course: CourseEntity) {
        viewModelScope.launch {
            courseDao.insertCourse(course)
            loadCourses()
        }
    }

    fun updateCourse(course: CourseEntity) {
        viewModelScope.launch {
            courseDao.updateCourse(course)
            loadCourses()
        }
    }
}

class CoursesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoursesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoursesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}