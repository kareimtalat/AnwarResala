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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.presentation.components.InputField
import com.kareimt.anwarresala.presentation.screens.Routes
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModel

@Composable
fun VolunteerCodeScreen(
    viewModel: VolunteerViewModel,
    context: Context,
    navController: NavController? = null,
) {
    if (viewModel.onError.isNotEmpty()) {
        Toast.makeText(context,viewModel.onError,Toast.LENGTH_LONG).show()
        viewModel.emptyOnError()
    }
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()+5.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(/*The second column is for Spaced by*/
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Activity label
                Text(
                    text = stringResource(R.string.volunteer_code),
                )


                // Activity code...
                InputField(
                    value = viewModel.activityCode,
                    onValueChange = viewModel::onActivityCodeChanged,
                    label = stringResource(R.string.activity_code),
                    rtl = false,
                    keyboardType = "Text"
                )

                //Volunteer Code...
                InputField(
                    value = viewModel.volunteerCode,
                    onValueChange = viewModel::onVolunteerCodeChanged,
                    label = stringResource(R.string.volunteer_code),
                    rtl = false,
                    keyboardType = "Text"
                )

                // Confirm Button
                Button(onClick = {
                    if (viewModel.activityCode == "19450" && viewModel.volunteerCode == "19450") {
                        navController?.navigate(Routes.LOGIN_SCREEN)
                    } else {
                        viewModel.showError(context,"Invalid Activity or Volunteer Code")
                    }
                }) { Text(stringResource(R.string.confirm)) }
            }

        }

}