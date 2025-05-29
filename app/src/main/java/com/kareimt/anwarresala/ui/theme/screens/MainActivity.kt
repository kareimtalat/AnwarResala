package com.kareimt.anwarresala.ui.theme.screens

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
// For the navigation
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.toCourse
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import com.kareimt.anwarresala.ui.theme.components.AddEditCourseScreen
import com.kareimt.anwarresala.ui.theme.screens.login_screens.RegistrationScreen
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.viewmodels.CoursesViewModelFactory
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel
import kotlinx.coroutines.time.delay
import java.time.Duration

class MainActivity : ComponentActivity() {
    private val viewModel: CoursesViewModel by viewModels { CoursesViewModelFactory(this) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Simple boolean for keeping splash screen
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        setContent {
            // Use LaunchedEffect for any initialization logic
            LaunchedEffect(Unit) {
                delay(Duration.ofMillis(1000))
                keepSplashScreen = false
            }

            AnwarResalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    AnwarResalaNavigation(viewModel,
                        volunteerViewModel = VolunteerViewModel(this)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    context: Context,
    navController: NavController,
    onBeneficiaryClick: () -> Unit,
    onVolunteerClick: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        )
        ) {

        // anwar_resala_logo
        Image(
            painter = painterResource(id = R.drawable.anwar_resala_logo),
            contentDescription = "Anwar Resala Logo",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 27.dp, end = 25.dp)
                .width(90.dp)
                // موازنة الإرتفاع مع العرض
                .aspectRatio(1f)
                //.clip(CircleShape)
        )

        // resala_logo
        Image(
            painter = painterResource(id = R.drawable.ressala_logo),
            contentDescription = "Resala Logo",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 33.dp, start = 25.dp)
                .width(90.dp)
                .aspectRatio(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = onBeneficiaryClick) {
                Text(text = context.getString(R.string.Beneficiary))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onVolunteerClick) {
                Text(text = context.getString(R.string.volunteer))
            }
        }
    }
}

// Navigation Routes
@Composable
fun AnwarResalaNavigation(coursesViewModel: CoursesViewModel=null, volunteerViewModel: VolunteerViewModel= null) {
    // To manage the navigation state
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.Main) {
        // TODO: Define every screen here

        // Main Screen
        composable(Routes.Main) {
            MainScreen(
                context = context,
                navController,
                onBeneficiaryClick = { navController.navigate(Routes.Beneficiary) },
                onVolunteerClick = { navController.navigate(Routes.Registration) }
            )
        }

        // Beneficiary Screen
        composable(Routes.Beneficiary) {
            HomeScreen(
                viewModel = coursesViewModel,
                onCourseClick = { course ->
                    navController.navigate(Routes.courseDetails(course.id))
                },
                onAddCourseClick = {
                    navController.navigate(Routes.addEditCourse())
                },
                onEditCourseClick = { courseId ->
                    navController.navigate(Routes.addEditCourse(courseId))
                },
            )
        }

        // Registration Screen
        composable(Routes.Registration) {
            RegistrationScreen(
                onBackClick = { navController.navigateUp() },
                viewModel = volunteerViewModel,
                context = context,
            )
        }

        // CourseDetailsScreen
        composable(
            route = Routes.CourseDetails,
            arguments = listOf(navArgument ("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Receive the value
            val courseId = backStackEntry.arguments?.getInt("courseId")
            LaunchedEffect(courseId) {
                courseId?.let { coursesViewModel.getCourseById(it) }
            }
            val course by coursesViewModel.selectedCourse.collectAsState()
            course?.let { courseEntity ->
                CourseDetailsScreen(
                    course = courseEntity.toCourse(),
                    onNavigateToEdit = { navController.navigate(Routes.addEditCourse(courseId)) },
                    onBack = { navController.navigateUp() },
                    onDeleteCourse = { navController.navigateUp() },
                    viewModel = coursesViewModel
                )
            }
        }

        // AddEditCourseScreen
        composable(
            route = Routes.AddEditCourse,
            arguments = listOf(navArgument("courseId") {
                type = NavType.IntType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId")
            AddEditCourseScreen(
                courseId = courseId,
                onBackClick = { navController.navigateUp() },
                viewModel = coursesViewModel
            )
        }
    }
}

































