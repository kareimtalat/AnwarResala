package com.kareimt.anwarresala.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.data.local.course.CourseEntity
import com.kareimt.anwarresala.data.local.DatabaseProvider
import com.kareimt.anwarresala.domain.repository.course.CourseRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CoursesViewModel(context:Context, private val repository: CourseRepositoryInterface) : ViewModel() {
    // Before connect firebase
    private val _uiState = MutableStateFlow(CoursesUiState())
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    // Update message
    fun updateMessage(message: String) {
        _uiState.update { it.copy(message = message) }
    }

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
                val query =_uiState.value.searchQuery.trim().lowercase(java.util.Locale.getDefault())
                val searchResults = if (query.isEmpty()){
                    courseDao.getAllCourses()
                }else{
                    courseDao.searchCourses(query)
                }
                updateCourses(searchResults)
            } catch (e: Exception) {
                setError(e.message?:"Error searching courses")
            }finally {
                setLoading(false)
            }
        }
    }

    // Handling Branches
    private val _branches = MutableStateFlow<List<BranchEntity>>(emptyList())
    val branches: StateFlow<List<BranchEntity>> = _branches.asStateFlow()

    private fun loadBranches() {
        viewModelScope.launch(Dispatchers.IO){
            _branches.value = branchDao.getAllBranches()
        }
    }

    fun addBranchLocally(branch: BranchEntity) {
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
    fun deleteBranchLocally(branch: BranchEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            branchDao.deleteBranch(branch)
            loadBranches()
        }
    }

    // Handling Courses
    private val _courses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val courses: StateFlow<List<CourseEntity>> = _courses

    private val courseDao = DatabaseProvider.getDatabase(context).courseDao()

    private val branchDao = DatabaseProvider.getBranchDatabase(context).branchDao()

    init {
        loadCourses()
        setCoursesListener()
        loadBranches()
        setupBranchesListener()
    }

    private fun loadCourses() {
        viewModelScope.launch(Dispatchers.IO){
            _courses.value = courseDao.getAllCourses()
        }
    }

    fun addCourse(course: CourseEntity) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addCourseOnFirebase(course).fold(
                        onSuccess = {
                            courseDao.insertCourse(course)
                            loadCourses()
                            setLoading(false)
                            updateMessage("Course added succesfully")
                        },
                        onFailure = { error ->
                            setError("Failed to add course: ${error.message}")
                            setLoading(false)
                        }
                    )
            } catch (e: Exception) {
                setError("Exception: ${e.message}")
                setLoading(false)
            }
        }
    }

    // For update exact course
    fun updateCourse(course: CourseEntity) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateCourseOnFirebase(course).fold(
                    onSuccess = {
                        courseDao.updateCourse(course)
                        loadCourses()
                        setLoading(false)
                        updateMessage("Course updated successfully")
                    },
                    onFailure = { error ->
                        setError("Failed to update course: ${error.message}")
                        setLoading(false)
                    }
                )
            } catch (e: Exception) {
                setError("Exception: ${e.message}")
                setLoading(false)
            }
        }
    }

    // For Delete exact course
    fun deleteCourse(course: CourseEntity) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.deleteCourse(course.id)
                if (result.isSuccess) {
                    courseDao.deleteCourse(course)
                    loadCourses()
                    withContext(Dispatchers.Main) {
                        setLoading(false)
                        updateMessage("Course deleted successfully")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        setLoading(false)
                        setError("Failed to delete course: ${result.exceptionOrNull()?.message}")
                    }
                }
            } catch (e: Exception) {
                withContext ( Dispatchers.Main) {
                    setLoading(false)
                    setError("Exception: ${e.message}")
                }
            }
        }
    }

    // For Getting course by ID
    private val _selectedCourse = MutableStateFlow<CourseEntity?>(null)
    val selectedCourse: StateFlow<CourseEntity?> = _selectedCourse.asStateFlow()
    fun getCourseById(courseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val course = courseDao.getCourseById(courseId)
            _selectedCourse.value = course
        }
    }

    // New only After add firebase
    // Attributes
    var onError by mutableStateOf("")
        private set

    // setters
    fun emptyOnError (){
        onError = ""
    }


    // getters


    // Normal functions
    // Branches functions
    fun addBranch(newBranch: String) {
        setLoading(true)
        try {
            viewModelScope.launch (Dispatchers.IO) {
                val result = repository.addBranchOnFirebase(newBranch)
                result.fold(
                    onSuccess = { branch ->
                        addBranchLocally(branch)
                    },
                    onFailure = { error ->
                        setError( "Fetching Current Volunteer Data failed: ${error.message}")
                    }
                )
            }
        } catch (e: Exception) {
            setError("Exception: ${e.message}")
        }
        setLoading(false)
    }

    fun setupBranchesListener() {
        repository.getBranches {
            repository.getBranches { result ->
                result.fold(
                    onSuccess = { branches ->
                        viewModelScope.launch (Dispatchers.IO) {
                            branchDao.deleteAllBranches()
                            branchDao.insertBranches(branches)
                            _branches.value = branches
                            println("Successfully loaded ${branches.size} branches")
                        }
                    },
                    onFailure = { error ->
                        println("BranchSync, Error: ${result.exceptionOrNull()}")
                        _uiState.update { it.copy(error = error.message) }
                    }
                )
            }
        }
    }

    fun removeBranchesListener() {
        repository.removeBranchesListener()
    }

    fun deleteBranch(branch: BranchEntity) {
        viewModelScope.launch (Dispatchers.IO) {
            val message = repository.deleteBranchOnFirebase(branch.id)
            withContext(Dispatchers.Main) {
                if (message.isEmpty()) {
                    deleteBranchLocally(branch)
                    updateMessage("Branch deleted successfully")
                } else {
                    _uiState.update { it.copy(error = "Failed to delete branch: $message") }
                }
            }
        }
    }

    fun setCoursesListener(){
        repository.getCourses { result ->
            result.fold(
                onSuccess = { courses ->
                    viewModelScope.launch (Dispatchers.IO){
                        courseDao.deleteAllCourses()
                        courseDao.insertCourses(courses)
                        _courses.value = courses
                        println("Successfully loaded ${courses.size} courses from the internet")
                    }
                },
                onFailure = { error ->
                    println("CourseSync, Error: ${result.exceptionOrNull()}")
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    fun removeCoursesListener() {
        repository.removeCoursesListener()
    }
}

class CoursesViewModelFactory(private val context: Context, private val repository: CourseRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoursesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoursesViewModel(
                context,
                repository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}