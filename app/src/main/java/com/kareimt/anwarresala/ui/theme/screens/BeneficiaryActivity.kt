package com.kareimt.anwarresala.ui.theme.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.jvm.java

class BeneficiaryActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // Perform any heavy initialization or data loading here

            // Once data is ready, set the content
            setContent {
                AnwarResalaTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        BeneficiaryActivityContent(context = this@BeneficiaryActivity)
                    }
                }
            }
        }
    }
}



@Composable
fun BeneficiaryActivityContent(context: Context){
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            // Get current month name in Arabic
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MMMM", Locale("ar")) // "MMMM" for full month name
            val currentMonthArabic = dateFormat.format(calendar.time)

            // Concatenate the strings
            val baseText = context.getString(R.string.the_current_courses_of_the_month)
            val combinedText = "$baseText $currentMonthArabic"

            // Display the combined text
            Text(
                text = combinedText,
                textAlign = TextAlign.End
            )

            var isFocused by remember { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy (8.dp)
            ){
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(text = "") },
                    modifier = Modifier.widthIn(max=300.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl),
                    trailingIcon = {
                        if (!isFocused) {
                            Text(
                                text = context.getString(R.string.search_in_all_courses),
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                )
                Button(onClick = {
                    // TODO Handle search button click
                    println("Search text: $searchText")
                }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_search),
                        contentDescription = "Search Icon"
                    )
                }
            }

            Button(onClick = {
                val intent = Intent(context, AllTheCourses::class.java)
                context.startActivity(intent)
            }) {Text(text = context.getString(R.string.all_the_courses)) }

            Button(onClick = {
                val intent = Intent(context, OnlineCourses::class.java)
                context.startActivity(intent)
            }) {Text(text = context.getString(R.string.online_courses)) }

            Button(onClick = {
                val intent = Intent(context, OfflineCourses::class.java)
                context.startActivity(intent)
            }) {Text(text = context.getString(R.string.offline_courses)) }

            Button(onClick = {
                val intent = Intent(context, BranchCoursesActivity::class.java)
                context.startActivity(intent)
            }) {Text(text = context.getString(R.string.branch_courses)) }




        }
    }
}