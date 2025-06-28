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
import com.kareimt.anwarresala.data.repository.FirebaseVolunteerRepository
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import com.kareimt.anwarresala.ui.theme.screens.Routes.addEditCourse
import com.kareimt.anwarresala.ui.theme.screens.beneficiary.AddEditCourseScreen
import com.kareimt.anwarresala.ui.theme.screens.beneficiary.BeneficiaryScreen
import com.kareimt.anwarresala.ui.theme.screens.beneficiary.ChooseBranchScreen
import com.kareimt.anwarresala.ui.theme.screens.beneficiary.CourseDetailsScreen
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.CoursesScreen
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.ScreenType
import com.kareimt.anwarresala.ui.theme.screens.login_screens.ForgetPasswordScreen
import com.kareimt.anwarresala.ui.theme.screens.login_screens.LoginScreen
import com.kareimt.anwarresala.ui.theme.screens.login_screens.RegistrationScreen
import com.kareimt.anwarresala.ui.theme.screens.login_screens.VolunteerCodeScreen
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.viewmodels.CoursesViewModelFactory
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel
import com.kareimt.anwarresala.viewmodels.VolunteerViewModelFactory
import kotlinx.coroutines.time.delay
import java.time.Duration
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private val coursesViewModel: CoursesViewModel by viewModels { CoursesViewModelFactory(this) }
    private val volunteerViewModel: VolunteerViewModel by viewModels { VolunteerViewModelFactory(FirebaseVolunteerRepository()) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        configureFirebaseServices()

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

    private fun configureFirebaseServices() {
        // Initialize Firebase Auth and Firestore
        FirebaseApp.initializeApp(this)
        Firebase.auth
        Firebase.firestore
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
                navController.navigate(Routes.BENEFICIARY)
            }
            ) { Text(text = context.getString(R.string.Beneficiary)) }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(Routes.VOLUNTEER_CODE)
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

    NavHost(navController = navController, startDestination = Routes.MAIN) {
        // Main Screen
        composable(Routes.MAIN) {
            MainScreen(
                context = context,
                navController
            )
        }

        // CoursesScreen
        composable(
            route = Routes.COURSES_SCREEN,
            arguments = listOf(
                navArgument("screenType") {
                    type = NavType.EnumType(ScreenType::class.java)
                    defaultValue = ScreenType.AllTheCourses
                                          },
                navArgument("branch") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getSerializable("screenType") as? ScreenType
                ?: ScreenType.AllTheCourses
            val branch = backStackEntry.arguments?.getString("branch") ?: ""

            CoursesScreen(
                courseViewModel = coursesViewModel,
                onAddCourseClick = { navController.navigate(addEditCourse(-1)) },
                context = context,
                screenType = screenType,
                navController = navController,
                searchQuery = "",
                branch = branch,
            ) { coursesViewModel.updateSearchQuery(it) }
        }

        // ForgetPasswordScreen
        composable(Routes.FORGET_PASSWORD) {
            ForgetPasswordScreen(
                context = context,
                onBackClick = { navController.navigateUp() },
                viewModel = volunteerViewModel
            )
        }

        // RegistrationScreen
        composable(Routes.REGISTRATION) {
            RegistrationScreen(
                viewModel = volunteerViewModel,
                context = context,
                navController = navController
            )
        }

        // Volunteer code Screen
        composable(Routes.VOLUNTEER_CODE) {
            VolunteerCodeScreen(
                viewModel = volunteerViewModel,
                context = context,
                navController = navController
            )
        }

        // LoginScreen
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(
                viewModel = volunteerViewModel,
                context = context,
                navController = navController
            )
        }

        // BeneficiaryScreen
        composable(Routes.BENEFICIARY) {
            BeneficiaryScreen(
                context = context,
                navController = navController,
                courseViewModel = coursesViewModel
            )
        }

        // ChooseBranchScreen
        composable(Routes.CHOOSE_BRANCH) {
            ChooseBranchScreen(
                context = context,
                navController = navController,
                searchQuery = "",
                onSearchQueryChange = { coursesViewModel.updateSearchQuery(it) },
                courseViewModel = coursesViewModel
            )
        }

        // CourseDetailsScreen
        composable(
            route = Routes.COURSE_DETAILS,
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
                    onBack = { navController.navigateUp() }
                )
            }
        }

        // AddEditCourseScreen
        composable(
            route = Routes.ADD_EDIT_COURSE,
            arguments = listOf(navArgument("courseId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: -1
            AddEditCourseScreen(
                courseId = courseId,
                onBackClick = { navController.navigateUp() },
                viewModel = coursesViewModel
            )
        }
    }
}