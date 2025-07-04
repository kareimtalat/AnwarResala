package com.kareimt.anwarresala.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.data.local.course.CourseEntity
import com.kareimt.anwarresala.data.local.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CoursesViewModel(context:Context) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Update search query
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // Handle loading state
    private fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(isLoading = loading) }
    }

    // Handle error state
    private fun setError(error: String?) {
        _uiState.update { it.copy(error = error) }
    }

    // Update courses list
    private fun updateCourses(courses: List<CourseEntity>) {
        _uiState.update { it.copy(courses = courses) }
    }

    // Search courses
    fun searchCourses(){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            try {
                val query =_uiState.value.searchQuery.trim()
                val searchResults = if (query.isEmpty()){
                    courseDao.getAllCourses()
                }else{
                    courseDao.searchCourses("%$query%")
                }
                updateCourses(searchResults)
            } catch (e: Exception) {
                setError(e.message?:"Error searching courses")
            }finally {
                setLoading(false)
            }
        }
    }

    // TODO: Complete the Cycle of adding and removing branches
    private val _branches = MutableStateFlow<List<BranchEntity>>(emptyList())
    val branches: StateFlow<List<BranchEntity>> = _branches.asStateFlow()

    private fun loadBranches() {
        viewModelScope.launch(Dispatchers.IO){
            _branches.value = branchDao.getAllBranches()
        }
    }

    fun addBranch(branch: BranchEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            branchDao.insertBranch(branch)
            _branches.value = branchDao.getAllBranches()
        }
    }

    // For update exact branch
    fun updateBranch(branch: BranchEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            branchDao.updateBranch(branch)
            loadBranches()
        }
    }

    // For Delete exact branch
    fun deleteBranch(branch: BranchEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            branchDao.deleteBranch(branch)
            loadBranches()
        }
    }

    private val _courses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val courses: StateFlow<List<CourseEntity>> = _courses

    private val courseDao = DatabaseProvider.getDatabase(context).courseDao()

    private val branchDao = DatabaseProvider.getBranchDatabase(context).branchDao()

    init {
        loadCourses()
        loadBranches()
    }

    private fun loadCourses() {
        viewModelScope.launch(Dispatchers.IO){
            _courses.value = courseDao.getAllCourses()
        }
    }

    fun addCourse(course: CourseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            courseDao.insertCourse(course)
            // Update both flows
            _courses.value = courseDao.getAllCourses()
            updateCourses(courseDao.getAllCourses())
        }
    }

    // For update exact course
    fun updateCourse(course: CourseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            courseDao.updateCourse(course)
            loadCourses()
        }
    }

    // For Delete exact course
    fun deleteCourse(course: CourseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            courseDao.deleteCourse(course)
            loadCourses()
        }
    }

    // For Getting course by ID
    private val _selectedCourse = MutableStateFlow<CourseEntity?>(null)
    val selectedCourse: StateFlow<CourseEntity?> = _selectedCourse.asStateFlow()
    fun getCourseById(courseId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val course = courseDao.getCourseById(courseId)
            _selectedCourse.value = course
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