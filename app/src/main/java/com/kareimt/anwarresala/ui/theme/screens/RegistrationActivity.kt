package com.kareimt.anwarresala.ui.theme.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.ui.theme.components.PasswordFieldWithToggle
import com.kareimt.anwarresala.ui.theme.components.ThePrompt
import com.kareimt.anwarresala.ui.theme.components.ReusableDropdown
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel
import com.kareimt.anwarresala.viewmodels.VolunteerViewModelFactory
import kotlinx.coroutines.launch


class RegistrationActivity: ComponentActivity(){
    val viewModel: VolunteerViewModel by viewModels { VolunteerViewModelFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // Perform any heavy initialization or data loading here

        // Once data is ready, set the content
        setContent{
            AnwarResalaTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    RegistrationContent(
                        viewModel=viewModel,
                        context = this@RegistrationActivity
                    )
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationContent(viewModel: VolunteerViewModel,context: Context) {
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
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent) },
                preText = context.getString(R.string.already_have_account),
                clickableText = context.getString(R.string.login))

            //NameField...
            InputField(
                value = viewModel.name,
                onValueChange = viewModel::onNameChanged,
                label = stringResource(R.string.nameLabel)
                )

            //ResponsibilityField...
            ReusableDropdown(
                label = stringResource(R.string.choose_your_responsibility),
                options = viewModel.getResponsibilityOptions(),
                value = viewModel.responsibility,
            ) { selectedOption ->
                viewModel.onResponsibilityChanged(selectedOption)
            }

            //BranchField...
            ReusableDropdown(
                label = stringResource(R.string.choose_your_branch),
                options = viewModel.getBranchOptions(),
                value = viewModel.branch.toString()
            ) { selectedOption ->
                viewModel.onBranchChanged(selectedOption)
            }

            //CommitteeField...
            ReusableDropdown(
                label = stringResource(R.string.choose_your_committee),
                options = viewModel.getCommitteeOptions(),
                value = viewModel.committee.toString()
            ) { selectedOption ->
                viewModel.onCommitteeChanged(selectedOption)
            }

            //EmailField...
            InputField(value = viewModel.email,
                onValueChange = viewModel::onEmailChanged,
                label = stringResource(R.string.email_label),
                keyboardType = "Email",
                rtl = false
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
                //TODO: what will happen when press Register. Will do to things:
//                1. Check the data validation and if right send it to the server and create account for the user.
//                2. Go to login activity.
            }) {Text(context.getString(R.string.register)) }



        }

        }
    }
