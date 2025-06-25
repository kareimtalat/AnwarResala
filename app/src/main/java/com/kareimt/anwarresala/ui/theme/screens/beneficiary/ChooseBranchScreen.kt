package com.kareimt.anwarresala.ui.theme.screens.beneficiary


import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.ui.theme.components.SearchRow
import com.kareimt.anwarresala.ui.theme.screens.Routes
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.ScreenType
import com.kareimt.anwarresala.viewmodels.CoursesViewModel

@Composable
fun ChooseBranchScreen(
    context: Context,
    navController: NavController,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit,
    courseViewModel: CoursesViewModel
) {
    var showAddBranchDialog by remember { mutableStateOf(false) }
    var newBranchName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = context.getString(R.string.all_the_branches),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Search bar
        // TODO: Modify it to search branches instead of courses
        SearchRow(
            query = searchQuery,
            onQueryChange = { onSearchQueryChange(it) },
            onSearch = { courseViewModel.searchCourses() }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Branches list
        val branches by courseViewModel.branches.collectAsState()
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ){
            items(branches) { branchEntity ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp, end = 95.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            navController.navigate(Routes.coursesScreen(ScreenType.BCSpecific, branchEntity.branch))
                        }
                    ) {
                        Text(
                            text = branchEntity.branch,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            courseViewModel.deleteBranch(branchEntity)
                        }
                    ) {
                        Text(
                            "-",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Add Branch Button
        Button(
            onClick = { showAddBranchDialog = true }
        ) {
            Text(
                text = "+",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                )
        }

        // Add Branch Dialog
        if (showAddBranchDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddBranchDialog = false
                    newBranchName = ""
                },
                title = { Text(
                    text = context.getString(R.string.add_new_branch),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()

                ) },
                text = {
                    OutlinedTextField(
                        value = newBranchName,
                        onValueChange = { newBranchName = it },
                        label = { Text(
                            text = context.getString(R.string.branch_name),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                            ) }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newBranchName.isNotBlank()) {
                                courseViewModel.addBranch(BranchEntity(branch = newBranchName))
                                newBranchName = ""
                                showAddBranchDialog = false
                            }
                        }
                    ) {
                        Text(text = context.getString(R.string.add))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showAddBranchDialog = false
                            newBranchName = ""
                        }
                    ) {
                        Text(text = context.getString(R.string.cancel))
                    }
                }
            )
        }



    }
}