package com.kareimt.anwarresala.ui.theme.screens


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.lifecycle.lifecycleScope
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.BCSpecificActivity
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel
import com.kareimt.anwarresala.viewmodels.VolunteerViewModelFactory
import kotlinx.coroutines.launch
import kotlin.getValue

// TODO: Handel this class from the beginning
class BranchCoursesActivity:ComponentActivity(){
    val viewModel: VolunteerViewModel by viewModels { VolunteerViewModelFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // Perform any heavy initialization or data loading here

            // Once data is ready, set the content
            setContent {
                AnwarResalaTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        BranchCoursesContent(
                            context = this@BranchCoursesActivity,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BranchCoursesContent(context: Context,viewModel: VolunteerViewModel) {
    var searchText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = context.getString(R.string.all_the_branches),
            textAlign = TextAlign.End
        )

        // Search Row
        var isFocused by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(text = "") },
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl),
                trailingIcon = {
                    if (!isFocused) {
                        Text(
                            text = context.getString(R.string.search_for_course),
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
        Spacer(modifier = Modifier.height(16.dp))

        // Courses list
        val branches= viewModel.getBranchOptions().filter { it != context.getString(R.string.central) }
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(branches) { branch ->
                Button(onClick = {
                    val intent = Intent(context, BCSpecificActivity::class.java).apply {
                        putExtra("branch", branch)
                    }
                    context.startActivity(intent)
                }) {
                    Text(
                        text = branch,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }




    }
}


