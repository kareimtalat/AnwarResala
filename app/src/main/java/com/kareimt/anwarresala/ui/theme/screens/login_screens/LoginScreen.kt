package com.kareimt.anwarresala.ui.theme.screens.login_screens

import android.content.Context
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
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.ui.theme.components.PasswordFieldWithToggle
import com.kareimt.anwarresala.ui.theme.components.ThePrompt
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel

//class LoginActivity: ComponentActivity(){
//    val viewModel: VolunteerViewModel by viewModels { VolunteerViewModelFactory() }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            // Perform any heavy initialization or data loading here
//
//            // Once data is ready, set the content
//            setContent {
//                AnwarResalaTheme {
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        LoginScreen(
//                            viewModel = viewModel,
//                            context = this@LoginActivity
//                        )
//                    }
//                }
//            }
//        }
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: VolunteerViewModel, context: Context,
    navController: NavController? = null,
    onBackClick: () -> Unit
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
                text = stringResource(R.string.login),
            )



            // EmailField...
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
                // TODO: Handel another password logic for the login
            )

            // Login Button
            Button(onClick = {
                //TODO: what will happen when press Login. Will do to things:
//                1. Check the data validation and if right send it to the server and login account for the user.
            }) {Text(context.getString(R.string.login)) }

            // Forget password Prompt
            ThePrompt(context = context,
                onClickFun =  {
                    navController?.navigate("forget_password_screen")
                     },
                preText = "",
                clickableText = context.getString(R.string.overwrite_password))

            // Register Prompt
            ThePrompt(context = context,
                onClickFun =  {
                    navController?.navigate("register_screen")
                    },
                preText = context.getString(R.string.dont_have_account),
                clickableText = context.getString(R.string.register))



        }

    }
}
