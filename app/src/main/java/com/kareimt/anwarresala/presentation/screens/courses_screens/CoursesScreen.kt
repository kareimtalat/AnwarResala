package com.kareimt.anwarresala.presentation.screens.courses_screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.course.CourseEntity
import com.kareimt.anwarresala.data.local.course.toCourse
import com.kareimt.anwarresala.presentation.components.AddCourseFloatingButton
import com.kareimt.anwarresala.presentation.components.CourseCard
import com.kareimt.anwarresala.presentation.components.SearchRow
import com.kareimt.anwarresala.presentation.screens.Routes
import com.kareimt.anwarresala.presentation.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.presentation.viewmodels.CoursesUiState
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModel

@Composable
fun CoursesScreen(
    courseViewModel: CoursesViewModel,
    onAddCourseClick: () -> Unit,
    context: Context,
    screenType: ScreenType,
    navController: NavController,
    branch: String = "",
    searchQuery: String,
    volunteerViewModel: VolunteerViewModel,
    onSearchQueryChange: (String) -> Unit,
    ) {
    val uiState by courseViewModel.uiState.collectAsState()

    if (uiState.message?.isNotEmpty()?:false) {
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        courseViewModel.updateMessage("")
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // The CircularProgressIndicator for loading the courses
        when {
            uiState.isLoading -> {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
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
                    branch = branch,
                    volunteerViewModel = volunteerViewModel
                )
            }
        }

        if ( volunteerViewModel.isLoggedIn && volunteerViewModel.currentVolunteer?.approved ?: false ) {
            AddCourseFloatingButton(
                modifier = Modifier
                    .padding(16.dp, 16.dp, 16.dp, 50.dp)
                    .align(Alignment.BottomEnd),
                onClick = onAddCourseClick
            )
        }
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
    branch: String,
    volunteerViewModel: VolunteerViewModel
){
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr)  {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()+5.dp
                ),
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
                branch = branch,
                volunteerViewModel = volunteerViewModel
            )
        }
    }
}

@Composable
fun CoursesScreen3(
    courseViewModel: CoursesViewModel,
    context: Context,
    searchQuery: String,
    navController: NavController,
    screenType: ScreenType,
    branch: String,
    volunteerViewModel: VolunteerViewModel
) {
    // Which courses will appear
    val courses: List<CourseEntity> = (if (searchQuery!="") {
        val uiState by courseViewModel.uiState.collectAsState(initial = CoursesUiState())
        uiState.courses
    }else{
        val coursesInGeneral by courseViewModel.courses.collectAsState()
        when {
            (screenType == ScreenType.OnLine) -> {
                coursesInGeneral.filter { courseEntity ->
                    courseEntity.type.toString().let { type ->
                        type == "ONLINE" || type == "HYBRID"
                    }
                }
            }
            (screenType == ScreenType.OffLine) -> {
                coursesInGeneral.filter { courseEntity ->
                    courseEntity.type.toString().let { type ->
                        type == "OFFLINE" || type == "HYBRID"
                    }
                }
            }
            (screenType == ScreenType.BCSpecific) -> {
                coursesInGeneral.filter { courseEntity ->
                    courseEntity.branch.trim() == branch.trim()
                }
            }
            else -> {
                coursesInGeneral
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
                    volunteerViewModel = volunteerViewModel
                )
            }
        }
    }
}