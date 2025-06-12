package com.kareimt.anwarresala.ui.theme.screens


import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.components.SearchRow
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel

//class BranchCoursesActivity:ComponentActivity(){
//    val viewModel: VolunteerViewModel by viewModels { VolunteerViewModelFactory() }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            // Perform any heavy initialization or data loading here
//
//            // Once data is ready, set the content
//            setContent {
//                AnwarResalaTheme {
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        BranchCoursesContent(
//                            context = this@BranchCoursesActivity,
//                            viewModel = viewModel
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun ChooseBranchScreen(
    context: Context,
    volunteerViewModel: VolunteerViewModel,
    navController: androidx.navigation.NavController,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit,
    courseViewModel: com.kareimt.anwarresala.viewmodels.CoursesViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = context.getString(R.string.all_the_branches),
            textAlign = TextAlign.End
        )

        // Search bar
        SearchRow(
            query = searchQuery,
            onQueryChange = { onSearchQueryChange(it) },
            onSearch = { courseViewModel.searchCourses() }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Branches list
        // TODO: Connect the branch list with the Room entity
        val branches= volunteerViewModel.getBranchOptions().filter { it != context.getString(R.string.central) }
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(branches) { branch ->
                Button(onClick = {
                    navController.navigate("branch_courses_screen/${branch}")
                }) {
                    Text(
                        text = branch,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }




    }
}


