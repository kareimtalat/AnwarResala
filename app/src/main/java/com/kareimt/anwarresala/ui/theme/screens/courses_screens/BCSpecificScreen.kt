package com.kareimt.anwarresala.ui.theme.screens.courses_screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.toCourse
import com.kareimt.anwarresala.ui.theme.components.AddCourseFloatingButton
import com.kareimt.anwarresala.ui.theme.components.CourseCard
import com.kareimt.anwarresala.ui.theme.components.SearchRow
import com.kareimt.anwarresala.ui.theme.screens.CourseDetailsActivity
import com.kareimt.anwarresala.viewmodels.CoursesViewModel

//class BCSpecificActivity:ComponentActivity(){
//    val viewModel: CoursesViewModel by viewModels { CoursesViewModelFactory(context = this) }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            // Perform any heavy initialization or data loading here
//
//            // Once data is ready, set the content
//            val branch = intent.getStringExtra("branch")
//            setContent {
//                AnwarResalaTheme {
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        BCSpecificScreen(
//                            context = this@BCSpecificActivity,
//                            viewModel = viewModel,
//                            branch = branch
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun BCSpecificScreen(context: Context, viewModel: CoursesViewModel, branch: String?) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BCSpecificContent(
            context = context,
            viewModel = viewModel,
            branch = branch
        )

        AddCourseFloatingButton(
            viewModel= viewModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}


@Composable
fun BCSpecificContent(context: Context,viewModel: CoursesViewModel, branch: String?) {
    // Courses list
    CoursesScreen5(
        viewModel = viewModel(),
        context= context,
        branch=branch
    )
}

@Composable
fun CoursesScreen5(viewModel: CoursesViewModel, context: Context,branch: String?) {
    // Activity label
    Text(
        text = context.getString(R.string.courses_of)+" $branch",
        textAlign = TextAlign.End
    )

    // Search bar
    var searchQuery by remember { mutableStateOf("") }
    SearchRow(
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = {
            // TODO: Handle search here
        }
    )

    // Courses list
    val coursesInGeneral by viewModel.courses.collectAsState()
    val courses = coursesInGeneral.filter { it.branch.toString() == branch }

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
                        val intent = Intent(context, CourseDetailsActivity::class.java).apply {
                            putExtra("course", course)
                        }
                        context.startActivity(intent)
                    },
                    viewModel
                )
            }
        }
    }
}
