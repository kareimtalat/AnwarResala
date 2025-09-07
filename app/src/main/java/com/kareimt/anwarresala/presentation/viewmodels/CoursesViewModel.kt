package com.kareimt.anwarresala.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.data.local.course.CourseEntity
import com.kareimt.anwarresala.data.local.DatabaseProvider
import com.kareimt.anwarresala.data.remote.repository.course.CourseRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CoursesViewModel(context:Context, private val repository: CourseRepositoryInterface) : ViewModel() {
    // Before connect firebase
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

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




    // After add firebase
    // Attributes
    var onError by mutableStateOf("")
        private set

    // setters
    fun emptyOnError (){
        onError = ""
    }


    // getters


    // Normal functions

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