package com.kareimt.anwarresala.presentation.screens.volunteer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.presentation.components.InputField
import com.kareimt.anwarresala.presentation.components.PasswordFieldWithToggle
import com.kareimt.anwarresala.presentation.components.ReusableDropdown
import com.kareimt.anwarresala.presentation.components.ThePrompt
import com.kareimt.anwarresala.presentation.screens.Routes
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: VolunteerViewModel,
    context: Context,
    navController: NavController
    ) {
    if (viewModel.onError.isNotEmpty()) {
        Toast.makeText(context, viewModel.onError, Toast.LENGTH_LONG).show()
        viewModel.emptyOnError()
    }
    // State to control the alert dialog visibility
    val showApprovalDialog = remember { mutableStateOf(false) }

    when {
        viewModel.isLoading -> {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        !viewModel.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding() + 5.dp
                        )
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Activity label
                        Text(
                            text = stringResource(R.string.volunteer_register),
                        )

                        // Login Prompt
                        ThePrompt(
                            onClickFun = {
                                navController.navigate("login_screen")
                            },
                            preText = context.getString(R.string.already_have_account),
                            clickableText = context.getString(R.string.login)
                        )

                        //Name Field...
                        InputField(
                            value = viewModel.name,
                            onValueChange = viewModel::onNameChanged,
                            label = stringResource(R.string.nameLabel),
                            rtl = false,
                            showRequired = true,
                            isError = viewModel.isNameError,
                        )

                        //Responsibility Field...
                        ReusableDropdown(
                            label = stringResource(R.string.choose_your_responsibility),
                            options = viewModel.getResponsibilityOptions(),
                            value = viewModel.responsibility,
                            onOptionSelected = { selectedOption ->
                                viewModel.onResponsibilityChanged(selectedOption)
                            },
                            showRequired = true,
                            isError = viewModel.isResponsibilityError
                        )

                        //Branch Field...
                        ReusableDropdown(
                            label = stringResource(R.string.choose_your_branch),
                            options = viewModel.getBranchOptions(),
                            value = viewModel.branch.toString(),
                            onOptionSelected = { selectedOption ->
                                viewModel.onBranchChanged(selectedOption)
                            },
                            showRequired = true,
                            isError = viewModel.isBranchError
                        )

                        //CommitteeField...
                        ReusableDropdown(
                            label = stringResource(R.string.choose_your_committee),
                            options = viewModel.getCommitteeOptions(),
                            value = viewModel.committee.toString(),
                            onOptionSelected = { selectedOption ->
                                viewModel.onCommitteeChanged(selectedOption)
                            },
                            showRequired = true,
                            isError = viewModel.isCommitteeError
                        )

                        //EmailField...
                        InputField(
                            value = viewModel.email,
                            onValueChange = viewModel::onEmailChanged,
                            label = stringResource(R.string.email_label),
                            rtl = false,
                            keyboardType = "Email",
                            showRequired = true,
                            isError = viewModel.isEmailError
                        )

                        //PasswordField
                        PasswordFieldWithToggle(
                            value = viewModel.password,
                            onValueChange = viewModel::onPasswordChanged,
                            label = stringResource(R.string.password_label),
                            showRequired = true,
                            isError = viewModel.isPasswordError,
                            labelToStart = "true",
                        )

                        //RePasswordField
                        PasswordFieldWithToggle(
                            value = viewModel.rePassword,
                            onValueChange = viewModel::onRePasswordChanged,
                            label = stringResource(R.string.renter_password_label),
                            imeAction = "Go",
                            showRequired = true,
                            isError = viewModel.isRePasswordError,
                            labelToStart = "true",
                        )

                        //Register Button
                        Button(onClick = {
                            if (viewModel.validateRegistrationData()) {
                                println("Form validation passed.")
                                viewModel.registerVolunteer(
                                    onSuccess = {
                                        showApprovalDialog.value = true
                                        println("Registration success callback executed.")
                                    },

                                    onError = { error ->
                                        println("Registration error: $error")
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    },
                                )
                            } else {
                                println("Form validation failed.")
                                Toast.makeText(context, R.string.registration_error, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        ) { Text(context.getString(R.string.register)) }
                    }
                }

            // Approval Alert Dialog
            if (showApprovalDialog.value) {
                AlertDialog(
                    onDismissRequest = {},
                    title = {
                        Text(text = stringResource(R.string.registration_submitted))
                    },
                    text = {
                        Text(text = stringResource(R.string.approval_pending_message))
                    },
                    confirmButton = {
                        Button(onClick = {
                            showApprovalDialog.value = false
                            navController.navigate(Routes.LOGIN_SCREEN) {
                                popUpTo(Routes.REGISTRATION) {
                                    inclusive = true
                                }
                            }
                        }) { Text(text = stringResource(R.string.ok)) }
                    }
                )
            }
        }
    }
}