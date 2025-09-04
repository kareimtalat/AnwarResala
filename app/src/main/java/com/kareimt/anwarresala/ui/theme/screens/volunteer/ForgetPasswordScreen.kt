package com.kareimt.anwarresala.ui.theme.screens.volunteer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordScreen(
    viewModel: VolunteerViewModel,
    context: Context,
    onBackClick: () -> Unit
) {
    if (viewModel.onError.isNotEmpty()) {
        Toast.makeText(context,viewModel.onError,Toast.LENGTH_LONG).show()
        viewModel.emptyOnError()
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr)  {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()+5.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Activity label
                Text(
                    text = stringResource(R.string.forget_password),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                //EmailField...
                InputField(
                    value = viewModel.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = stringResource(R.string.email_label),
                    rtl = false,
                    keyboardType = "Email"
                )

                // Confirm Button
                Button(onClick = {
                    //TODO: what will happen when press confirm_email. Will do to things:
    //                1. Send message to the email to reset password
    //                2. Show a message to the user that the email has been sent
    //                3. Navigate to the new screen
                }) {Text(context.getString(R.string.confirm_email)) }



            }

        }
    }
}
