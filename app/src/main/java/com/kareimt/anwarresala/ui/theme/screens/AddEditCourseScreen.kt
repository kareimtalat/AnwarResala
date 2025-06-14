package com.kareimt.anwarresala.ui.theme.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.Course.Instructor
import com.kareimt.anwarresala.data.Course.Organizer
import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.data.toCourse
import com.kareimt.anwarresala.data.toEntity
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.ui.theme.components.ReusableDropdown
import com.kareimt.anwarresala.viewmodels.CoursesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddEditCourseScreen(
    courseId: Int,
    onBackClick: () -> Unit,
    viewModel: CoursesViewModel,
) {
    var course: Course? = null
    LaunchedEffect(courseId) {
        if (courseId != -1) {
            viewModel.getCourseById(courseId)
        }
    }
    val selectedCourse by viewModel.selectedCourse.collectAsState()
    course = selectedCourse?.toCourse()
    if (courseId == -1) { course = null }

    var category by remember { mutableStateOf(course?.category ?: "") }
    var title by remember { mutableStateOf(course?.title ?: "") }
    var branch by remember { mutableStateOf(course?.branch ?: "") }
    // TODO: use the branches state -Room Entity- after handling it's functionality circle completely
    //val branches: State<List<BranchEntity>> =
    //    viewModel.branches.collectAsState(initial = emptyList())
    var type by remember { mutableStateOf(course?.type ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf(course?.imagePath ?: "drawable/anwar_resala_logo") }
    var instructorName by remember { mutableStateOf(course?.instructor?.name ?: "") }
    var instructorBio by remember { mutableStateOf(course?.instructor?.bio ?: "") }
    var instructorImg by remember {
        mutableStateOf(
            course?.instructor?.imagePath?.toString() ?: "drawable/anwar_resala_logo"
        )
    }
    var startDate by remember { mutableStateOf(course?.startDate ?: "") }
    var wGLink by remember { mutableStateOf(course?.wGLink ?: "") }
    var courseDetails by remember { mutableStateOf(course?.courseDetails ?: "") }
    var totalLectures by remember {
        mutableStateOf(
            course?.totalLectures?.toString() ?: ""
        )
    }
    var noOfLiteraturesFinished by remember {
        mutableStateOf(
            course?.noOfLiteraturesFinished?.toString() ?: ""
        )
    }
    var nextLecture by remember { mutableStateOf(course?.nextLecture ?: "") }
    var organizerName by remember { mutableStateOf(course?.organizer?.name ?: "") }
    var organizerWhats by remember { mutableStateOf(course?.organizer?.whatsapp ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (courseId == -1) "Add Course" else "Edit Course") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ID
            if (course != null) {
                Text(
                    text = "Course ID: ${course.id}",
                )
            }

            val branches by viewModel.branches.collectAsState()

            // Error states for validation
            var titleError by remember { mutableStateOf(false) }
            var branchError by remember { mutableStateOf(false) }
            var categoryError by remember { mutableStateOf(false) }
            var typeError by remember { mutableStateOf(false) }
            var instructorNameError by remember { mutableStateOf(false) }
            var instructorBioError by remember { mutableStateOf(false) }
            var startDateError by remember { mutableStateOf(false) }
            var totalLecturesError by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.padding(10.dp))
            // Title
//            Text(text = "(* Required Field)", color = Color.Red, modifier = Modifier.align(Alignment.Start))
            InputField(
                value = course?.title ?: title,
                onValueChange = { title = it; titleError = false },
                label = "Title",
                isError = titleError,
                showRequired = true,
            )
//            if (titleError) {
//                Text("This field is required", color = Color.Red, style = MaterialTheme.typography.bodySmall)
//            }
            Spacer(modifier = Modifier.padding(10.dp))

            // Branch
            var showAddbranchDialog by remember { mutableStateOf(false) }
            var newBranchName by remember { mutableStateOf("") }
            var newBranchError by remember { mutableStateOf(false) }

            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReusableDropdown(
                        modifier = Modifier.weight(1f),
                        label = "Branch",
                        options = branches.map { it.branch },
                        value = branch,
                        onOptionSelected = { selectedBranch ->
                            branch = selectedBranch; branchError = false
                        },
                        isError = branchError,
                        showRequired = true
                    )
                    IconButton(onClick = { showAddbranchDialog = true}) {
                        Icon(Icons.Default.Add, "Add Branch")
                    }
                }

                // Add Branch Dialog
                if (showAddbranchDialog){
                    AlertDialog(
                        onDismissRequest = {
                            showAddbranchDialog = false
                            newBranchName = ""
                            newBranchError = false
                                           },
                        title = { Text("Add New Branch") },
                        text = {
                            InputField(
                                value = newBranchName,
                                onValueChange = { newBranchName = it; newBranchError = false },
                                label = "Branch Name",
                                isError = newBranchError,
                                showRequired = true
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (newBranchName.isBlank()) {
                                        newBranchError = true
                                        return@Button
                                    }
                                    viewModel.addBranch(BranchEntity(branch = newBranchName))
                                    showAddbranchDialog = false
                                    newBranchName = ""
                                }) {
                                Text("Add")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showAddbranchDialog = false
                                newBranchName = ""
                                newBranchError = false
                            }) {
                                Text("Cancel")
                            }
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))


            // Course Image
            InputField(
                value = course?.imagePath ?: imagePath,
                onValueChange = { imagePath = it },
                label = "Course Image Path",
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Category
//            Text(text = "(* Required Field)", color = Color.Red, modifier = Modifier.align(Alignment.Start))
            InputField(
                value = course?.category ?: category,
                onValueChange = { category = it; categoryError = false },
                label = "Category",
                isError = categoryError,
                showRequired = true
                )
//            if (categoryError) {
//                Text("This field is required", color = Color.Red, style = MaterialTheme.typography.bodySmall)
//            }
            Spacer(modifier = Modifier.padding(10.dp))


            // Course Type
//            Text(text = "(* Required Field)", color = Color.Red, modifier = Modifier.align(Alignment.Start))
            val courseTypes = CourseType.entries.map { it.name }
            ReusableDropdown(
                label = "Course Type",
                options = courseTypes,
                value = type.toString(),
                onOptionSelected = { selectedType ->
                    type = CourseType.valueOf(selectedType); typeError = false
                },
                isError = typeError,
                showRequired = true
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Instructor Name
//            Text(text = "(* Required Field)", color = Color.Red, modifier = Modifier.align(Alignment.Start))
            InputField(
                value = course?.instructor?.name ?: instructorName,
                onValueChange = { instructorName = it; instructorNameError = false },
                label = "Instructor Name",
                isError = instructorNameError,
                showRequired = true
            )
//            if (titleError) {
//                Text("This field is required", color = Color.Red, style = MaterialTheme.typography.bodySmall)
//            }
            Spacer(modifier = Modifier.padding(10.dp))


            // Instructor Biography
//            Text(text = "(* Required Field)", color = Color.Red, modifier = Modifier.align(Alignment.Start))
            InputField(
                value = course?.instructor?.bio ?: instructorBio,
                onValueChange = { instructorBio = it; instructorBioError = false },
                label = "Instructor Biography",
                isError = instructorBioError,
                showRequired = true
            )
//            if (instructorBioError) {
//                Text("This field is required", color = Color.Red, style = MaterialTheme.typography.bodySmall)
//            }
            Spacer(modifier = Modifier.padding(10.dp))


            // Instructor Image Path
            InputField(
                value = course?.instructor?.imagePath ?: instructorImg,
                onValueChange = { instructorImg = it },
                label = "Instructor Image Path"
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Course Start Date
//            Text(text = "(* Required Field)", color = Color.Red, modifier = Modifier.align(Alignment.Start))
            InputField(
                value = course?.startDate ?: startDate,
                onValueChange = { startDate = it; startDateError = false },
                label = "Course Start Date",
                isError = startDateError,
                showRequired = true
            )
//            if (startDateError) {
//                Text("This field is required", color = Color.Red, style = MaterialTheme.typography.bodySmall)
//            }
            Spacer(modifier = Modifier.padding(10.dp))


            // Course whatsapp Group Link
            InputField(
                value = course?.wGLink ?: wGLink,
                onValueChange = { wGLink = it },
                label = "Course whatsapp Group Link"
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Course Details
            InputField(
                value = course?.courseDetails ?: courseDetails,
                onValueChange = { courseDetails = it },
                label = "Course Details"
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Number of literatures
//            Text(text = "(* Required Field)", color = Color.Red, modifier = Modifier.align(Alignment.Start))
            InputField(
                value = (course?.totalLectures ?: totalLectures).toString(),
                onValueChange = { totalLectures = it; totalLecturesError= false },  // Now it's String to String
                label = "Total Lectures",
                isError = totalLecturesError,
                showRequired = true
            )
//            if (totalLecturesError) {
//                Text("This field is required", color = Color.Red, style = MaterialTheme.typography.bodySmall)
//            }
            Spacer(modifier = Modifier.padding(10.dp))


            // Number of literatures finished
            InputField(
                value = (course?.noOfLiteraturesFinished ?: noOfLiteraturesFinished).toString(),
                onValueChange = { noOfLiteraturesFinished = it },  // Now it's String to String
                label = "Number of literatures finished",
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Next Lecture appointment
            InputField(
                value = course?.nextLecture ?: nextLecture,
                onValueChange = { nextLecture = it },
                label = "Next Lecture appointment",
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Organizer Name
            InputField(
                value = course?.organizer?.name ?: organizerName,
                onValueChange = { organizerName = it },  // Now it's String to String
                label = "Organizer Name",
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Organizer Whatsapp
            InputField(
                value = course?.organizer?.whatsapp ?: organizerWhats,
                onValueChange = { organizerWhats = it },  // Now it's String to String
                label = "Organizer Whatsapp",
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // When creating the Course object, convert types appropriately
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Cancel Button
                TextButton(onClick = onBackClick) {
                    Text("Cancel")
                }

                val context = LocalContext.current
                // Save or Add Button
                Button(
                    onClick = {
                        // Validate required fields
                        titleError = title.isBlank()
                        branchError = branch.isBlank()
                        categoryError = category.isBlank()
                        typeError = type.toString().isBlank()
                        instructorNameError = instructorName.isBlank()
                        instructorBioError = instructorBio.isBlank()
                        startDateError = startDate.isBlank()
                        totalLecturesError = totalLectures.isBlank() || totalLectures.toIntOrNull() == null
                        if (titleError || branchError || categoryError || typeError ||
                            instructorNameError || instructorBioError || startDateError ||
                            totalLecturesError) {
                            // Show error messages
                            Toast.makeText(context,"Please fill all required fields correctly.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val newCourse = Course(
                            id = 0,
                            title = title,
                            branch = branch,
                            category = category,
                            type = type as CourseType,
                            imagePath = when {
                                selectedImageUri != null -> selectedImageUri.toString()
                                else -> imagePath
                            },
                            instructor = Instructor(
                                name = instructorName,
                                bio = instructorBio,
                                imagePath = when {
                                    selectedImageUri != null -> selectedImageUri.toString()
                                    else -> imagePath
                                },
                            ),
                            startDate = startDate,
                            wGLink = wGLink,
                            courseDetails = courseDetails,
                            totalLectures = totalLectures.toIntOrNull() ?: 0,
                            noOfLiteraturesFinished = noOfLiteraturesFinished.toIntOrNull()
                                ?: 0,
                            nextLecture = nextLecture,
                            organizer = Organizer(
                                name = organizerName,
                                whatsapp = organizerWhats
                            ),
                        )
                        if (course == null) {
                            viewModel.addCourse(newCourse.toEntity())
                        } else {
                            viewModel.updateCourse(newCourse.toEntity())
                        }
                        onBackClick()
                    },
                ) {
                    Text(if (courseId != -1) "Add" else "Save")
                }
                // one } for the Row
            }
        }
    }
}

// The floating (+) button to add a new course
@Composable
fun AddCourseFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Course"
        )
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String = "Confirm",
    dismissText: String = "Dismiss"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text=title)},
        text = { Text(text=message)},
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}