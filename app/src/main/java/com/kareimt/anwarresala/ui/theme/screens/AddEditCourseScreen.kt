package com.kareimt.anwarresala.ui.theme.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.Course.Instructor
import com.kareimt.anwarresala.data.Course.Organizer
import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.data.toCourse
import com.kareimt.anwarresala.data.toEntity
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

    var category by remember { mutableStateOf(course?.category ?: "") }
    var title by remember { mutableStateOf(course?.title ?: "") }
    var branch by remember { mutableStateOf(course?.branch ?: "") }
    // TODO: use the branches state -Room Entity- after handling it's functionality circle completely
    //val branches: State<List<BranchEntity>> =
    //    viewModel.branches.collectAsState(initial = emptyList())
    var type by remember { mutableStateOf(course?.type ?: CourseType.ONLINE) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf(course?.imagePath ?: "drawable/anwar_resala_logo") }
    var instructorName by remember { mutableStateOf(course?.instructor?.name ?: "") }  // Fix this
    var instructorBio by remember { mutableStateOf(course?.instructor?.bio ?: "") }  // Fix this
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
                .padding(16.dp)
        ) {
            // ID
            if (course != null) {
                Text(
                    text = "Course ID: ${course.id}",
                )
            }

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Branch
            OutlinedTextField(
                value = branch,
                onValueChange = { branch = it },
                label = { Text("Branch") },
                modifier = Modifier.fillMaxWidth()
            )

            // Course Image
            OutlinedTextField(
                value = imagePath,
                onValueChange = { imagePath = it },
                label = { Text("Course Image Path") },
                modifier = Modifier.fillMaxWidth()
            )

            // Category
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            // Course Type
            val courseTypes = CourseType.values().map { it.name }
            ReusableDropdown(
                label = "Course Type",
                options = courseTypes,
                value = type.name,
                onOptionSelected = { selectedType ->
                    type = CourseType.valueOf(selectedType)
                }
            )

            // Instructor Name
            OutlinedTextField(
                value = instructorName,
                onValueChange = { instructorName = it },
                label = { Text("Instructor Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Instructor Biography
            OutlinedTextField(
                value = instructorBio,
                onValueChange = { instructorBio = it },
                label = { Text("Instructor Biography") },
                modifier = Modifier.fillMaxWidth()
            )

            // Instructor Image Path
            OutlinedTextField(
                value = instructorImg,
                onValueChange = { instructorImg = it },
                label = { Text("Instructor Image Path") },
                modifier = Modifier.fillMaxWidth()
            )

            // Course Start Date
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text("Course Start Date") },
                modifier = Modifier.fillMaxWidth()
            )

            // Course whatsapp Group Link
            OutlinedTextField(
                value = wGLink,
                onValueChange = { wGLink = it },
                label = { Text("Course whatsapp Group Link") },
                modifier = Modifier.fillMaxWidth()
            )

            // Course Details
            OutlinedTextField(
                value = courseDetails,
                onValueChange = { courseDetails = it },
                label = { Text("Course Details") },
                modifier = Modifier.fillMaxWidth()
            )

            // Number of literatures
            OutlinedTextField(
                value = totalLectures,
                onValueChange = { totalLectures = it },  // Now it's String to String
                label = { Text("Total Lectures") },
                modifier = Modifier.fillMaxWidth()
            )

            // Number of literatures finished
            OutlinedTextField(
                value = noOfLiteraturesFinished,
                onValueChange = { noOfLiteraturesFinished = it },  // Now it's String to String
                label = { Text("Number of literatures finished") },
                modifier = Modifier.fillMaxWidth()
            )

            // Next Lecture appointment
            OutlinedTextField(
                value = nextLecture,
                onValueChange = { nextLecture = it },  // Now it's String to String
                label = { Text("Next Lecture appointment") },
                modifier = Modifier.fillMaxWidth()
            )

            // Organizer Name
            OutlinedTextField(
                value = organizerName,
                onValueChange = { organizerName = it },  // Now it's String to String
                label = { Text("Organizer Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Organizer Whatsapp
            OutlinedTextField(
                value = organizerWhats,
                onValueChange = { organizerWhats = it },  // Now it's String to String
                label = { Text("Organizer Whatsapp") },
                modifier = Modifier.fillMaxWidth()
            )

            // When creating the Course object, convert types appropriately
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Cancel Button
                TextButton(onClick = onBackClick) {
                    Text("Cancel")
                }

                // Save or Add Button
                Button(
                    onClick = {
                        val newCourse = Course(
                            id = courseId ?: 0,
                            title = title,
                            branch = branch,
                            category = category,
                            type = type,
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
                            )
                        )
                        if (course == null) {
                            viewModel.addCourse(newCourse.toEntity())
                        } else {
                            viewModel.updateCourse(newCourse.toEntity())
                        }
                        onBackClick()
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
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
        onClick = {onClick},
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