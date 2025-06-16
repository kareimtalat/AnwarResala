package com.kareimt.anwarresala.ui.theme.screens

// For the navigation
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.course.toCourse
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import com.kareimt.anwarresala.ui.theme.screens.Routes.addEditCourse
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.CoursesScreen
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.ScreenType
import com.kareimt.anwarresala.ui.theme.screens.login_screens.ForgetPasswordScreen
import com.kareimt.anwarresala.ui.theme.screens.login_screens.LoginScreen
import com.kareimt.anwarresala.ui.theme.screens.login_screens.RegistrationScreen
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.viewmodels.CoursesViewModelFactory
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel
import com.kareimt.anwarresala.viewmodels.VolunteerViewModelFactory
import kotlinx.coroutines.time.delay
import java.time.Duration

class MainActivity : ComponentActivity() {
    private val coursesViewModel: CoursesViewModel by viewModels { CoursesViewModelFactory(this) }
    private val volunteerViewModel: VolunteerViewModel by viewModels { VolunteerViewModelFactory() }

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
                    AnwarResalaNavigation(coursesViewModel, volunteerViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(context: Context, navController: NavController) {
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
            Button(onClick = {
                navController.navigate(Routes.Beneficiary)
            }
            ) { Text(text = context.getString(R.string.Beneficiary)) }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(Routes.LoginScreen)
            }
            ) { Text(text = context.getString(R.string.volunteer)) }
        }
    }
}

// Navigation Routes
@Composable
fun AnwarResalaNavigation(
    coursesViewModel: CoursesViewModel,
    volunteerViewModel: VolunteerViewModel,
    ) {
    // To manage the navigation state
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.Main) {
        // Main Screen
        composable(Routes.Main) {
            MainScreen(
                context = context,
                navController
            )
        }

        // CoursesScreen
        composable(Routes.CoursesScreen) {
            CoursesScreen(
                courseViewModel = coursesViewModel,
                onAddCourseClick = { navController.navigate(addEditCourse(-1)) },
                context = context,
                screenType = ScreenType.AllTheCourses,
                navController = navController,
                searchQuery = "",
            ) { coursesViewModel.updateSearchQuery(it) }
        }

        // ForgetPasswordScreen
        composable(Routes.ForgetPassword) {
            ForgetPasswordScreen(
                context = context,
                onBackClick = { navController.navigateUp() },
                viewModel = volunteerViewModel
            )
        }

        // RegistrationScreen
        composable(Routes.Registration) {
            RegistrationScreen(
                viewModel = volunteerViewModel,
                context = context,
                onBackClick = { navController.navigateUp() },
                navController = navController
            )
        }

        // LoginScreen
        composable(Routes.LoginScreen) {
            LoginScreen(
                viewModel = volunteerViewModel,
                context = context,
                onBackClick = { navController.navigateUp() },
                navController = navController
            )
        }

        // BeneficiaryScreen
        composable(Routes.Beneficiary) {
            BeneficiaryScreen(
                context = context,
                navController = navController,
                courseViewModel = coursesViewModel
            )
        }

        // ChooseBranchScreen
        composable(Routes.ChooseBranch) {
            ChooseBranchScreen(
                context = context,
                navController = navController,
                volunteerViewModel = volunteerViewModel,
                searchQuery = "",
                onSearchQueryChange = { coursesViewModel.updateSearchQuery(it) },
                courseViewModel = coursesViewModel
            )
        }

        // CourseDetailsScreen
        composable(
            route = Routes.CourseDetails,
            arguments = listOf(navArgument ("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Receive the value
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: -1
            LaunchedEffect(courseId) {
                courseId.let { coursesViewModel.getCourseById(it) }
            }
            val course by coursesViewModel.selectedCourse.collectAsState()
            course?.let { courseEntity ->
                CourseDetailsScreen(
                    course = courseEntity.toCourse(),
                    viewModel = coursesViewModel,
                    onNavigateToEdit = { navController.navigate(addEditCourse(courseId)) },
                    onBack = { navController.navigateUp() },
                    onDeleteCourse = { navController.navigateUp() },
                    navController = navController
                )
            }
        }

        // AddEditCourseScreen
        composable(
            route = Routes.AddEditCourse,
            arguments = listOf(navArgument("courseId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            var courseId = backStackEntry.arguments?.getInt("courseId") ?: -1
            AddEditCourseScreen(
                courseId = courseId,
                onBackClick = { navController.navigateUp() },
                viewModel = coursesViewModel
            )
        }
    }
}