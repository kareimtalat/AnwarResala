package com.kareimt.anwarresala.ui.theme.screens.courses_screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.toCourse
import com.kareimt.anwarresala.data.toEntity
import com.kareimt.anwarresala.ui.theme.components.AddCourseFloatingButton
import com.kareimt.anwarresala.ui.theme.components.AddEditCourseScreen
import com.kareimt.anwarresala.ui.theme.components.CourseCard
import com.kareimt.anwarresala.ui.theme.components.SearchRow
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.viewmodels.UiState


//class AllTheCourses : ComponentActivity() {
//    private val viewModel: CoursesViewModel by viewModels { CoursesViewModelFactory(context = this) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            AnwarResalaTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    AllTheCoursesScreen(
//                        context = this@AllTheCourses,
//                        viewModel = viewModel
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun AllTheCoursesScreen(
    context: Context,
    viewModel: CoursesViewModel
) {
    val uiState by viewModel.uiState.collectAsState(initial = UiState())

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // The CircularProgressIndicator for loading the courses
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                AllTheCoursesContent(
                    context = context,
                    viewModel = viewModel,
                    searchQuery = uiState.searchQuery,
                    modifier= Modifier.align(Alignment.Center),
                ) { viewModel.updateSearchQuery(it) }

            }
        }

        // The FloatingActionButton to add a new course
        AddCourseFloatingButton(
            viewModel= viewModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )

        // The dialog for adding/editing course
        if (uiState.showAddDialog) {
            AddEditCourseScreen(
                courseId = null,
                viewModel = viewModel,
                onDismiss = { viewModel.hideAddCourseDialog() },
                onSaveClick = { course ->
                    viewModel.addCourse(course.toEntity())
                    viewModel.hideAddCourseDialog()
                }
            )
        }
    }
}

@Composable
fun AllTheCoursesContent(
    context: Context,
    viewModel: CoursesViewModel,
    searchQuery: String,
    modifier: Modifier,
    onSearchQueryChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Activity label
        Text(
            text = context.getString(R.string.all_the_courses),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
        )

        // Search bar
        SearchRow(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = {viewModel.searchCourses() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Courses list
        CoursesScreen(
            viewModel = viewModel,
            context= context,
            searchQuery = searchQuery
        )


    }
}

@Composable
fun CoursesScreen(viewModel: CoursesViewModel, context: Context, searchQuery: String,) {

    val courses = if (searchQuery!="") {
        val uiState by viewModel.uiState.collectAsState(initial = UiState())
        uiState.courses
    }else{
        val coursesInGeneral by viewModel.courses.collectAsState()
        coursesInGeneral
    }

    if (courses.isEmpty()) {
        Text(
            text = context.getString(R.string.no_courses_found),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn {
            items(courses) { courseEntity ->
                val course = courseEntity.toCourse()
                CourseCard(
                    course = course,
                    onItemClick = {
                        // TODO: solve the navController issue
                        navController.navigate("courseDetails/${course.id}")

//                        val intent = Intent(context, CourseDetailsActivity::class.java).apply {
//                            putExtra("course", course)
//                        }
//                        context.startActivity(intent)
                    },
                    viewModel
                )
            }
        }
    }
}

