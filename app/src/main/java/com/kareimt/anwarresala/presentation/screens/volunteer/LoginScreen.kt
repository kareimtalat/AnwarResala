package com.kareimt.anwarresala.presentation.screens.volunteer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.presentation.components.InputField
import com.kareimt.anwarresala.presentation.components.PasswordFieldWithToggle
import com.kareimt.anwarresala.presentation.components.ThePrompt
import com.kareimt.anwarresala.presentation.screens.Routes
import com.kareimt.anwarresala.presentation.viewmodels.UiEvent
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModel
import kotlinx.coroutines.delay

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: VolunteerViewModel, context: Context,
    navController: NavController? = null
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.eventsFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (viewModel.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
        if (viewModel.onError.isNotEmpty()) {
            Toast.makeText(context, viewModel.onError, Toast.LENGTH_LONG).show()
//            Text(
//                text = viewModel.onError,
//                color = Color.Red,
//            )
            LaunchedEffect(viewModel.onError) {
                delay(6000)
                viewModel.emptyOnError()
            }
        }
        viewModel.getCurrentVolunteer()
        val currentVolunteer = viewModel.currentVolunteer

        Box (
            modifier = Modifier.fillMaxWidth()
        ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding() + 5.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(/*The second column is for Spaced by*/
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //Screen label
                Text(
                    text = stringResource(R.string.login),
                )

                // EmailField...
                InputField(
                    value = viewModel.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = stringResource(R.string.email_label),
                    rtl = false,
                    keyboardType = "Email",
                    labelToStart = "true"
                )

                //PasswordField
                PasswordFieldWithToggle(
                    value = viewModel.password,
                    onValueChange = viewModel::onPasswordChanged,
                    label = stringResource(R.string.password_label),
                    labelToStart = "true"
                )

                // Login Button
                Button(onClick = {
                    if (currentVolunteer?.email?.isNotEmpty() ?: false && currentVolunteer.email != viewModel.email) {
                        viewModel.deleteCurrentVolunteerLocally()
                    }
                    try {
                        if (viewModel.validateLoginData()) {
//                                    if (currentVolunteer==null) {
//                                        Toast.makeText(context,R.string.dont_have_account_locally,Toast.LENGTH_LONG).show()
//                                    }
                            if (currentVolunteer?.approved ?: false || currentVolunteer == null) {
                                viewModel.loginVolunteer(
                                    onSuccess = {
                                        navController?.navigate(Routes.MAIN) {
                                            popUpTo(Routes.LOGIN_SCREEN) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onError = { errorMessage ->
//                                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
//                                            .show()
                                    }
                                )
                            } else {
                                Toast.makeText(context, R.string.not_apptoved, Toast.LENGTH_LONG)
                                    .show()
                            }
                        } else {
                            Toast.makeText(context, R.string.registration_error, Toast.LENGTH_LONG)
                                .show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                    //                    Firebase.analytics.logEvent("login_button_clicked", null)
                }) { Text(context.getString(R.string.login)) }

                // Forget password Prompt
//                    ThePrompt(
//                        onClickFun =  {
//                            navController?.navigate(Routes.FORGET_PASSWORD)
//                             },
//                        preText = "",
//                        clickableText = context.getString(R.string.overwrite_password))

                // Register Prompt
                ThePrompt(
                    onClickFun = {
                        navController?.navigate(Routes.REGISTRATION)
                    },
                    preText = context.getString(R.string.dont_have_account),
                    clickableText = context.getString(R.string.register)
                )
            }

        }

        if (viewModel.isLoading) {
            Column (
                modifier = Modifier
                    .padding(innerPadding)
                    .wrapContentSize()
                    .padding(20.dp)
//                        .fillMaxSize()
                    .background(Color(0x99DED7D7)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        }
    }
}
