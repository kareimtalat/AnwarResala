package com.kareimt.anwarresala.ui.theme.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.ui.theme.components.SearchRow
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.ScreenType
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//class BeneficiaryActivity: ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            // Perform any heavy initialization or data loading here
//
//            // Once data is ready, set the content
//            setContent {
//                AnwarResalaTheme {
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        BeneficiaryActivityContent(context = this@BeneficiaryActivity)
//                    }
//                }
//            }
//        }
//    }
//}



@Composable
fun BeneficiaryScreen(
    context: Context,
    navController: NavController,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    courseViewModel: CoursesViewModel
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            // Get current month name in Arabic
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MMMM", Locale("ar")) // "MMMM" for full month name
            val currentMonthArabic = dateFormat.format(calendar.time)

            // Concatenate the strings
            val baseText = context.getString(R.string.the_current_courses_of_the_month)
            val combinedText = "$baseText $currentMonthArabic"

            // Display the combined text
            Text(
                text = combinedText,
                textAlign = TextAlign.End
            )

            // Search bar
            SearchRow(
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it) },
                onSearch = { courseViewModel.searchCourses() }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Buttons for navigation
            Button(onClick = {
                navController.navigate(Routes.coursesScreen(ScreenType.AllTheCourses))
            }) {Text(text = context.getString(R.string.all_the_courses)) }

            Button(onClick = {
                navController.navigate(Routes.coursesScreen(ScreenType.OnLine))
            }) {Text(text = context.getString(R.string.online_courses)) }

            Button(onClick = {
                navController.navigate(Routes.coursesScreen(ScreenType.OffLine))
            }) {Text(text = context.getString(R.string.offline_courses)) }

            Button(onClick = {
                navController.navigate(Routes.ChooseBranch)
            }) {Text(text = context.getString(R.string.branch_courses)) }
        }
    }
}