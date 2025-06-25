package com.kareimt.anwarresala.ui.theme.screens.login_screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.ui.theme.screens.Routes
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel

@Composable
fun VolunteerCodeScreen(
    viewModel: VolunteerViewModel,
    context: Context,
    navController: NavController? = null,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(/*The second column is for Spaced by*/
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Activity label
            Text(
                text = "Volunteer Code",
            )


            // Activity code...
            InputField(
                value = viewModel.activityCode,
                onValueChange = viewModel::onActivityCodeChanged,
                label = "Activity Code",
                rtl = false,
                keyboardType = "Text"
            )

            //Volunteer Code...
            InputField(
                value = viewModel.volunteerCode,
                onValueChange = viewModel::onVolunteerCodeChanged,
                label = "Volunteer Code",
                rtl = false,
                keyboardType = "Text"
            )

            // Confirm Button
            Button(onClick = {
                if (viewModel.activityCode == "19450" && viewModel.volunteerCode == "19450") {
                    navController?.navigate(Routes.LoginScreen)
                } else {
                    viewModel.showError(context,"Invalid Activity or Volunteer Code")
                }
            }) { Text("Confirm") }
        }

    }
}