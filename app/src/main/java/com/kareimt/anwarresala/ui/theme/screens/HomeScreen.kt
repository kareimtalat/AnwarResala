package com.kareimt.anwarresala.ui.theme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.ui.theme.components.CourseCard
import com.kareimt.anwarresala.ui.theme.components.SearchRow
import com.kareimt.anwarresala.viewmodels.CoursesViewModel

@Composable
fun HomeScreen(
    viewModel: CoursesViewModel,
    onCourseClick: (Course) -> Unit,
    onAddCourseClick: () -> Unit,
    onEditCourseClick: (Int) -> Unit,
    ) {
    val courses by viewModel.courses.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchRow(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onSearch = { viewModel.searchCourses() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(courses) { courseEntity ->
                    CourseCard(
                        course = courseEntity.toCourse(),
                        onItemClick = { onCourseClick(courseEntity.toCourse()) },
                        onEditClick = { onEditCourseClick(courseEntity.id) },
                        viewModel = viewModel,
                )
            }
        }
    }

    FloatingActionButton(
        onClick = onAddCourseClick,
        modifier = Modifier
            .padding(16.dp)
            .align(Alignment.BottomEnd)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Course")
    }
}

    CourseCard(
        course = course,
        onItemClick = { /* your item click handling */ },
        onEditClick = { courseId -> onEditCourseClick(courseId) },  // Pass the callback
        viewModel = viewModel
    )
}
























