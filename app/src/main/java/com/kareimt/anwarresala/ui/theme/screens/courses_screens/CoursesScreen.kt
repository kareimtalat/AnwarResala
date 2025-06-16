package com.kareimt.anwarresala.ui.theme.screens.courses_screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.ui.theme.components.CourseCard
import com.kareimt.anwarresala.ui.theme.components.SearchRow
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.data.local.course.toCourse
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.course.CourseEntity
import com.kareimt.anwarresala.ui.theme.screens.AddCourseFloatingButton
import com.kareimt.anwarresala.ui.theme.screens.Routes
import com.kareimt.anwarresala.viewmodels.UiState

@Composable
fun CoursesScreen(
    courseViewModel: CoursesViewModel,
    onAddCourseClick: () -> Unit,
    context: Context,
    screenType: ScreenType,
    navController: NavController,
    branch: String = "",
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    ) {
    val uiState by courseViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // The CircularProgressIndicator for loading the courses
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Error at loading courses",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp),
                )
            }
            else -> {
                CoursesScreen2(
                    context = context,
                    courseViewModel = courseViewModel,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { onSearchQueryChange(it) },
                    screenType,
                    navController = navController,
                    branch = branch
                )

            }
        }



        AddCourseFloatingButton(
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 50.dp)
                .align(Alignment.BottomEnd),
            onClick = onAddCourseClick
        )
    }
}

enum class ScreenType {
    AllTheCourses, OffLine, OnLine, BCSpecific
}

@Composable
fun CoursesScreen2(
    context: Context,
    courseViewModel: CoursesViewModel,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    screenType: ScreenType,
    navController: NavController,
    branch: String
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Screen label
        Text(
            text = when {
                (screenType == ScreenType.AllTheCourses) -> {
                    context.getString(R.string.all_the_courses)
                }
                (screenType == ScreenType.OnLine) -> {
                       context.getString(R.string.online_courses)
                       }
                (screenType == ScreenType.OffLine) -> {
                    context.getString(R.string.offline_courses)
                }
                else -> {
                    context.getString(R.string.courses_of) + " $branch"
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 16.dp)
                .fillMaxWidth()
        )

        // Search bar
        SearchRow(
            query = searchQuery,
            onQueryChange = { onSearchQueryChange(it) },
            onSearch = { courseViewModel.searchCourses() }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Courses list
        CoursesScreen3(
            courseViewModel = courseViewModel,
            context= context,
            searchQuery = searchQuery,
            navController = navController,
            screenType = screenType,
            branch = branch
        )
    }
}

@Composable
fun CoursesScreen3(
    courseViewModel: CoursesViewModel,
    context: Context,
    searchQuery: String,
    navController: NavController,
    screenType: ScreenType,
    branch: String
) {
    val branches by courseViewModel.branches.collectAsState()

    // Which courses will appear
    var courses = emptyList<CourseEntity>()
    courses = (if (searchQuery!="") {
        val uiState by courseViewModel.uiState.collectAsState(initial = UiState())
        uiState.courses
    }else{
        val coursesInGeneral by courseViewModel.courses.collectAsState()
        when {
            (screenType == ScreenType.AllTheCourses) -> {
                coursesInGeneral
            }
            (screenType == ScreenType.OnLine) -> {
                coursesInGeneral.filter { it.type.toString()=="ONLINE" || it.type.toString()=="HYBRID" }
            }
            (screenType == ScreenType.OffLine) -> {
                coursesInGeneral.filter { it.type.toString()=="OFFLINE" || it.type.toString()=="HYBRID" }
            }
            else -> {
                coursesInGeneral.filter { it.branch.toString() == branch }
            }
        }
    })

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
                        navController.navigate(Routes.courseDetails(course.id))
                    },
                    viewModel = courseViewModel,
                    onEditClick = { navController.navigate(Routes.addEditCourse(course.id)) },
                )
            }
        }
    }
}