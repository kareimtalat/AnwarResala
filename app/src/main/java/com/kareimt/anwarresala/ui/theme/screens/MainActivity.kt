package com.kareimt.anwarresala.ui.theme.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.firestore.util.Util
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import kotlinx.coroutines.time.delay
import java.time.Duration

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var isLoading by mutableStateOf(true)

        // Keep the splash screen visible until loading is complete
        splashScreen.setKeepOnScreenCondition { isLoading }
        setContent {

                var isLoadingState by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(Duration.ofMillis(1000))
                    isLoadingState = false
                    isLoading = false
                }
            AnwarResalaTheme {
                if (isLoading) {
                    // Show a loading screen or placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column (verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.anwar_resala_logo),
                                contentDescription = "Anwar Resala Logo",
                                modifier = Modifier
                                    //.padding(27.dp)
                                    .width(130.dp)
                                    .aspectRatio(1f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = stringResource(R.string.loading),
                                modifier = Modifier.wrapContentWidth(),
                                //textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium)

                        }
//                        CircularProgressIndicator(
//                            modifier = Modifier.size(48.dp), // Adjust size as needed
//                            color = MaterialTheme.colorScheme.primary, // Customize color
//                            strokeWidth = 4.dp // Customize stroke width
//                        )
                    }
                } else {
                    // Main content goes here
                    // For example, you can show your main screen or (navigation graph)***
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background) {
                        MainScreen(
                        context = this@MainActivity
                    )
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(context: Context) {
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
                val intent = Intent(context, BeneficiaryActivity::class.java)
                context.startActivity(intent)
            }
            ) { Text(text = context.getString(R.string.Beneficiary)) }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(context, RegistrationActivity::class.java)
                context.startActivity(intent)
            }
            ) { Text(text = context.getString(R.string.volunteer)) }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AnwarResalaTheme {
//        MainScreen()
//    }
//}