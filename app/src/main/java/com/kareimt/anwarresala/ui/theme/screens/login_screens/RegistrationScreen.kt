package com.kareimt.anwarresala.ui.theme.screens.login_screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.ui.theme.components.PasswordFieldWithToggle
import com.kareimt.anwarresala.ui.theme.components.ThePrompt
import com.kareimt.anwarresala.ui.theme.components.ReusableDropdown
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: VolunteerViewModel,
    context: Context,
    navController: androidx.navigation.NavController
    ) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
            ThePrompt(context = context,
                onClickFun =  {
                    navController.navigate("login_screen")
                              },
                preText = context.getString(R.string.already_have_account),
                clickableText = context.getString(R.string.login))

            //NameField...
            InputField(
                value = viewModel.name,
                onValueChange = viewModel::onNameChanged,
                label = stringResource(R.string.nameLabel),
                rtl = false
            )

            //ResponsibilityField...
            ReusableDropdown(
                label = stringResource(R.string.choose_your_responsibility),
                options = viewModel.getResponsibilityOptions(),
                value = viewModel.responsibility,
                onOptionSelected = { selectedOption ->
                    viewModel.onResponsibilityChanged(selectedOption)
                },
            )

            //BranchField...
            ReusableDropdown(
                label = stringResource(R.string.choose_your_branch),
                options = viewModel.getBranchOptions(),
                value = viewModel.branch.toString(),
                onOptionSelected = { selectedOption ->
                    viewModel.onBranchChanged(selectedOption)
                }
            )

            //CommitteeField...
            ReusableDropdown(
                label = stringResource(R.string.choose_your_committee),
                options = viewModel.getCommitteeOptions(),
                value = viewModel.committee.toString(),
                onOptionSelected = { selectedOption ->
                    viewModel.onCommitteeChanged(selectedOption)
                }
            )

            //EmailField...
            InputField(
                value = viewModel.email,
                onValueChange = viewModel::onEmailChanged,
                label = stringResource(R.string.email_label),
                rtl = false,
                keyboardType = "Email"
            )

            //PasswordField
            PasswordFieldWithToggle(
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChanged,
                label = stringResource(R.string.password_label),
            )

            //RePasswordField
            PasswordFieldWithToggle(
                value = viewModel.rePassword,
                onValueChange = viewModel::onRePasswordChanged,
                label = stringResource(R.string.renter_password_label),
                imeAction="Go",
            )

            //Register Button
            Button(onClick = {
                //TODO: what will happen when press Register. Will do two things:
//                1. Check the data validation and if right send it to the server and create account for the user.
//                2. Go to login activity.
                if (viewModel.validateRegistrationData()) {
                    viewModel.registerVolunteer(
                    onSuccess = {
                        // Show success message
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                        // Navigate to login screen
                        navController.navigate("login_screen"){
                            // Clear back stack to prevent going back to registration screen
                            popUpTo("registration_screen") { inclusive = true}
                        }
                    },
                    onError = { error->
                        // Show error message
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                    )
                }
            }) {Text(context.getString(R.string.register)) }
        }

        }
    }