package com.kareimt.anwarresala.ui.theme.screens

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.Course.Instructor
import com.kareimt.anwarresala.data.Course.Organizer
import com.kareimt.anwarresala.data.CourseType
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
    var type by remember { mutableStateOf(course?.type ?: CourseType.ONLINE) }
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
            verticalArrangement = Arrangement.spacedBy(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ID
            if (course != null) {
                Text(
                    text = "Course ID: ${course.id}",
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))

            // Title
            InputField(
                value = course?.title ?: title,
                onValueChange = { title = it },
                label = "Title",
            )

            // Branch
            InputField(
                value = course?.branch ?: branch,
                onValueChange = { branch = it },
                label = "Branch",
            )

            // Course Image
            InputField(
                value = course?.imagePath ?: imagePath,
                onValueChange = { imagePath = it },
                label = "Course Image Path",
            )

            // Category
            InputField(
                value = course?.category ?: category,
                onValueChange = { category = it },
                label = "Category",
                )

            // Course Type
            val courseTypes = CourseType.entries.map { it.name }
            ReusableDropdown(
                label = "Course Type",
                options = courseTypes,
                value = type.name,
                onOptionSelected = { selectedType ->
                    type = CourseType.valueOf(selectedType)
                }
            )

            // Instructor Name
            InputField(
                value = course?.instructor?.name ?: instructorName,
                onValueChange = { instructorName = it },
                label = "Instructor Name"
            )

            // Instructor Biography
            InputField(
                value = course?.instructor?.bio ?: instructorBio,
                onValueChange = { instructorBio = it },
                label = "Instructor Biography"
            )

            // Instructor Image Path
            InputField(
                value = course?.instructor?.imagePath ?: instructorImg,
                onValueChange = { instructorImg = it },
                label = "Instructor Image Path"
            )

            // Course Start Date
            InputField(
                value = course?.startDate ?: startDate,
                onValueChange = { startDate = it },
                label = "Course Start Date"
            )

            // Course whatsapp Group Link
            InputField(
                value = course?.wGLink ?: wGLink,
                onValueChange = { wGLink = it },
                label = "Course whatsapp Group Link"
            )

            // Course Details
            InputField(
                value = course?.courseDetails ?: courseDetails,
                onValueChange = { courseDetails = it },
                label = "Course Details"
            )

            // Number of literatures
            InputField(
                value = (course?.totalLectures ?: totalLectures).toString(),
                onValueChange = { totalLectures = it },  // Now it's String to String
                label = "Total Lectures"
            )

            // Number of literatures finished
            InputField(
                value = (course?.noOfLiteraturesFinished ?: noOfLiteraturesFinished).toString(),
                onValueChange = { noOfLiteraturesFinished = it },  // Now it's String to String
                label = "Number of literatures finished"
            )

            // Next Lecture appointment
            InputField(
                value = course?.nextLecture ?: nextLecture,
                onValueChange = { nextLecture = it },
                label = "Next Lecture appointment",
            )

            // Organizer Name
            InputField(
                value = course?.organizer?.name ?: organizerName,
                onValueChange = { organizerName = it },  // Now it's String to String
                label = "Organizer Name"
            )

            // Organizer Whatsapp
            InputField(
                value = course?.organizer?.whatsapp ?: organizerWhats,
                onValueChange = { organizerWhats = it },  // Now it's String to String
                label = "Organizer Whatsapp"
            )

            // When creating the Course object, convert types appropriately
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 20.dp),
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
                            id = courseId,
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