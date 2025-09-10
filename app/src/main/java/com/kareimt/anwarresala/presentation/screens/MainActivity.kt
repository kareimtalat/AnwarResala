package com.kareimt.anwarresala.presentation.screens

// For the navigation

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.DatabaseProvider
import com.kareimt.anwarresala.data.local.course.toCourse
import com.kareimt.anwarresala.domain.repository.course.CourseRepository
import com.kareimt.anwarresala.domain.repository.volunteer.VolunteerRepository
import com.kareimt.anwarresala.presentation.components.ActionConfirmationDialog
import com.kareimt.anwarresala.presentation.components.ReusableAlertDialog
import com.kareimt.anwarresala.presentation.screens.Routes.addEditCourse
import com.kareimt.anwarresala.presentation.screens.beneficiary.AddEditCourseScreen
import com.kareimt.anwarresala.presentation.screens.beneficiary.BeneficiaryScreen
import com.kareimt.anwarresala.presentation.screens.beneficiary.ChooseBranchScreen
import com.kareimt.anwarresala.presentation.screens.beneficiary.CourseDetailsScreen
import com.kareimt.anwarresala.presentation.screens.courses_screens.CoursesScreen
import com.kareimt.anwarresala.presentation.screens.courses_screens.ScreenType
import com.kareimt.anwarresala.presentation.screens.volunteer.ApprovalScreen
import com.kareimt.anwarresala.presentation.screens.volunteer.ForgetPasswordScreen
import com.kareimt.anwarresala.presentation.screens.volunteer.LoginScreen
import com.kareimt.anwarresala.presentation.screens.volunteer.RegistrationScreen
import com.kareimt.anwarresala.presentation.screens.volunteer.VolunteerCodeScreen
import com.kareimt.anwarresala.presentation.viewmodels.ApprovalViewModel
import com.kareimt.anwarresala.presentation.viewmodels.ApprovalViewModelFactory
import com.kareimt.anwarresala.presentation.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.presentation.viewmodels.CoursesViewModelFactory
import com.kareimt.anwarresala.presentation.viewmodels.UiEvent
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModel
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModelFactory
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import com.kareimt.anwarresala.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.util.Locale

class MainActivity : ComponentActivity() {
    val coursesRepository = CourseRepository()
    private val coursesViewModel: CoursesViewModel by viewModels { CoursesViewModelFactory(
        this,
        repository = coursesRepository
    ) }
    private lateinit var volunteerViewModel: VolunteerViewModel
//    private lateinit var approvalViewModel: ApprovalViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        configureFirebaseServices()

        // Initialize the DAO and ViewModel
        val volunteerDao = DatabaseProvider.getVolunteerDatabase(this).volunteerDao()
        volunteerViewModel = ViewModelProvider(this,
            VolunteerViewModelFactory(VolunteerRepository(), volunteerDao, coursesViewModel)
        )[VolunteerViewModel::class.java]

        // Make app Rtl layout and arabic language only
        val locale = Locale("ar")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)

        // storeCurrentVolunteerLocally
        val isLoggedIn = volunteerViewModel.isLoggedIn
        volunteerViewModel.getCurrentVolunteer()
        val isApproved = volunteerViewModel.currentVolunteer?.approved ?: false
        if (isLoggedIn && volunteerViewModel.currentVolunteer == null || !isApproved) {
            volunteerViewModel.storeCurrentVolunteerLocally()
        }

        // Get Firebase quota
        volunteerViewModel.setIsFirebaseQuotaExceeded()

        // Splash screen keeping boolean
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
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnwarResalaNavigation(coursesViewModel, volunteerViewModel)
                }
            }
        }
    }

    // Get the branch entities
    override fun onStart() {
        super.onStart()
        coursesViewModel.setupBranchesListener()
    }

    override fun onStop() {
        super.onStop()
        coursesViewModel.removeBranchesListener()
        println("Branches Listeners removed")
        coursesViewModel.removeCoursesListener()
        println("Courses Listeners removed")
    }

    private fun configureFirebaseServices() {
        // Initialize Firebase Auth and Firestore
        FirebaseApp.initializeApp(this)
        Firebase.auth
        Firebase.firestore
        Firebase.storage
    }
}

@Composable
fun MainScreen(context: Context, navController: NavController, volunteerViewModel: VolunteerViewModel) {
    val isLoggedIn = volunteerViewModel.isLoggedIn
    var showDelAccountDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    val isFirebaseQuotaExceeded = volunteerViewModel.isFirebaseQuotaExceeded
    val activity = LocalActivity.current
    val shouldExitApp = volunteerViewModel.shouldExitApp
    var isOnline by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect events from ViewModel
    LaunchedEffect(Unit) {
        volunteerViewModel.eventsFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    if (volunteerViewModel.onError.isNotEmpty()) {
        Toast.makeText(context, volunteerViewModel.onError, Toast.LENGTH_LONG).show()
        LaunchedEffect(volunteerViewModel.onError) {
            delay(6000)
            volunteerViewModel.emptyOnError()
        }
    }


    // Check internet connection on composition and periodically
    LaunchedEffect(Unit) /*(Dispatchers.IO)*/ {
        while (true) {
            isOnline = NetworkUtils.isNetworkAvailable(context)
            delay(5000)
        }
    }

    if (shouldExitApp) {
        println(" LaunchedEffect triggering")
        LaunchedEffect(Unit) {
            activity?.finish()
            volunteerViewModel.resetExitRequest()
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 5.dp
            )
            ) {
            // Show connection status at the top
            if (!isOnline) {
                Box (
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16229A))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.trying_to_connect_internet),
                        modifier = Modifier.wrapContentWidth(),
                        color = Color(0xDEFF1210),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            if (isFirebaseQuotaExceeded) {
                ReusableAlertDialog(
                    title = stringResource(R.string.alert),
                    message = stringResource(R.string.firebaseQuotaExceeded),
                    onDismiss = { volunteerViewModel.requestAppExit() },
                    onConfirm = { volunteerViewModel.requestAppExit() },
                    confirmText = stringResource(R.string.ok),
                )
            }

            if (volunteerViewModel.isLoading) {
                Column (
                    modifier = Modifier
                        .padding(innerPadding)
                        .wrapContentSize()
                        .padding(20.dp)
                        .wrapContentSize()
                        .background(Color(0x99DED7D7)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            if (volunteerViewModel.isLoggedIn) {
                val fullName = volunteerViewModel.currentVolunteer?.name
                val firstName = fullName?.split(" ")?.firstOrNull() ?: fullName
                Text(
                    context.getString(R.string.welcome)+" $firstName",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 70.dp),
                    )
            }

            // anwar_resala_logo
            Column (
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 27.dp, start = 25.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.anwar_resala_logo),
                    contentDescription = "Anwar Resala Logo",
                    modifier = Modifier
                        .width(90.dp)
                        // موازنة الإرتفاع مع العرض
                        .aspectRatio(1f)
                        //.clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(14.dp))

                if (isLoggedIn) {
                    IconButton( onClick = {showSignOutDialog = true} ) { Icon( painter = painterResource(R.drawable.ic_sign_out), contentDescription = "Sign Out", tint = Color.Unspecified ) }
                }
            }

            // resala_logo
            Column (
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 33.dp, end = 25.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ressala_logo),
                    contentDescription = "Resala Logo",
                    modifier = Modifier
                        .width(90.dp)
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isLoggedIn) {
                    IconButton( onClick = {showDelAccountDialog = true} , modifier = Modifier.align ( Alignment.End )) { Icon( painter = painterResource(R.drawable.ic_delete_account), contentDescription = "Delete Account", tint = Color.Unspecified ) }
                }
            }

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
                    val destination = if (isLoggedIn && volunteerViewModel.currentVolunteer!=null) Routes.APPROVAL else Routes.VOLUNTEER_CODE
                    if (isLoggedIn && volunteerViewModel.currentVolunteer?.responsibility == context.getString(R.string.committee_member)) {
                        Toast.makeText(context, context.getString(R.string.you_are_already_loggedin), Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate(destination)
                }
                }
                ) { Text(text = context.getString(R.string.volunteer)) }

                if (showSignOutDialog) {
                    ActionConfirmationDialog(
                        title = stringResource(R.string.sign_out),
                        message = stringResource(R.string.sign_out_des),
                        confirmText = stringResource(R.string.sign_out),
                        dismissText = stringResource(R.string.cancel),
                        onConfirm = {
                            volunteerViewModel.logout()
                            showSignOutDialog = false
                                    },
                        onDismiss = { showSignOutDialog = false }
                    )
                }

                if (showDelAccountDialog){
                    ActionConfirmationDialog(
                        title = stringResource(R.string.delete_account),
                        message = stringResource(R.string.delete_account_confirmation),
                        confirmText = stringResource(R.string.delete),
                        dismissText = stringResource(R.string.cancel),
                        onConfirm = {
                            volunteerViewModel.deleteAccount(context)
                            showDelAccountDialog = false
                                    },
                        onDismiss = { showDelAccountDialog = false }
                    )
                }
            }
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
                navController,
                volunteerViewModel = volunteerViewModel
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
                onAddCourseClick = { navController.navigate(addEditCourse("-1")) },
                context = context,
                screenType = screenType,
                navController = navController,
                searchQuery = "",
                branch = branch,
                volunteerViewModel = volunteerViewModel,
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
                courseViewModel = coursesViewModel,
                volunteerViewModel = volunteerViewModel,
            )
        }

        // CourseDetailsScreen
        composable(
            route = Routes.COURSE_DETAILS,
            arguments = listOf(navArgument ("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Receive the value
            val courseId = backStackEntry.arguments?.getString("courseId") ?: "-1"
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
                    volunteerViewModel = volunteerViewModel
                )
            }
        }

        // AddEditCourseScreen
        composable(
            route = Routes.ADD_EDIT_COURSE,
            arguments = listOf(navArgument("courseId") {
                type = NavType.StringType
                defaultValue = "-1"
            })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: "-1"
            AddEditCourseScreen(
                courseId = courseId,
                onBackClick = { navController.navigateUp() },
                viewModel = coursesViewModel,
                volunteerViewModel,
                context
            )
        }

        // Approval Screen
        composable (Routes.APPROVAL) {
            val context = LocalContext.current
            val application = context.applicationContext as Application
            val volunteerDao = DatabaseProvider.getVolunteerDatabase(application).volunteerDao()
            val viewModel: ApprovalViewModel = viewModel(factory = ApprovalViewModelFactory(application, volunteerDao))
            ApprovalScreen(viewModel = viewModel, navController = navController)
        }
    }
}