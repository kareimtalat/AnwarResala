package com.kareimt.anwarresala.ui.theme.screens.beneficiary

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.Course.Instructor
import com.kareimt.anwarresala.data.Course.Organizer
import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.data.local.course.toCourse
import com.kareimt.anwarresala.data.local.course.toEntity
import com.kareimt.anwarresala.ui.theme.components.BranchField
import com.kareimt.anwarresala.ui.theme.components.SelectingImageField
import com.kareimt.anwarresala.ui.theme.components.CoursesOfMonthField
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.ui.theme.components.SelectingTimeDateField
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

    var category by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var coursesOfMonth by remember { mutableStateOf(emptyList<String>()) }
    var type by remember { mutableStateOf<CourseType?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf("") }
    var instructorName by remember { mutableStateOf("") }
    var instructorBio by remember { mutableStateOf("") }
    var instructorImg by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var wGLink by remember { mutableStateOf("") }
    var courseDetails by remember { mutableStateOf("") }
    var totalLectures by remember { mutableStateOf("") }
    var noOfLiteraturesFinished by remember { mutableStateOf("") }
    var nextLecture by remember { mutableStateOf("") }
    var organizerName by remember { mutableStateOf("") }
    var organizerWhats by remember { mutableStateOf("") }

    if (courseId!=-1) {
        LaunchedEffect(selectedCourse) {
            selectedCourse?.let { courseEntity ->
                val course = courseEntity.toCourse()
                category = course.category
                title = course.title
                branch = course.branch
                coursesOfMonth = course.coursesOfMonth
                type = course.type
                imagePath = course.imagePath
                instructorName = course.instructor.name
                instructorBio = course.instructor.bio
                instructorImg = course.instructor.imagePath
                startDate = course.startDate
                wGLink = course.wGLink
                courseDetails = course.courseDetails
                totalLectures = course.totalLectures.toString()
                noOfLiteraturesFinished = course.noOfLiteraturesFinished.toString()
                nextLecture = course.nextLecture
                organizerName = course.organizer.name
                organizerWhats = course.organizer.whatsapp
            }
        }
    }

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
            Spacer(modifier = Modifier.padding(10.dp))

            // Error states for validation
            var titleError by remember { mutableStateOf(false) }
            var branchError by remember { mutableStateOf(false) }
            var categoryError by remember { mutableStateOf(false) }
            var typeError by remember { mutableStateOf(false) }
            var instructorNameError by remember { mutableStateOf(false) }
            var instructorBioError by remember { mutableStateOf(false) }
            var coursesOfMonthError by remember { mutableStateOf(false) }
            var totalLecturesError by remember { mutableStateOf(false) }

            // Title
            InputField(
                value = title,
                onValueChange = { title = it; titleError = false },
                label = "Title",
                isError = titleError,
                showRequired = true,
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Branch
            BranchField(
                viewModel = viewModel,
                branch = branch,
                onOptionSelected = { selectedBranch ->
                    branch = selectedBranch; branchError = false
                },
                branchError = branchError,
            )

            // CoursesOfMonth
            CoursesOfMonthField(
                course = course,
                coursesOfMonth = { coursesOfMonth = it; coursesOfMonthError = false },
                coursesOfMonthError = coursesOfMonthError,
                onErrorChange = { coursesOfMonthError = it }
            )

            // Course Image
            SelectingImageField(
                imagePath = imagePath,
                onImagePathChange = {imagePath = it },
                "Course Image"
            )

            // Category
            InputField(
                value = category,
                onValueChange = { category = it; categoryError = false },
                label = "Category",
                isError = categoryError,
                showRequired = true
                )
            Spacer(modifier = Modifier.padding(10.dp))

            // Course Type
            val courseTypeDisplayMap = mapOf(
                CourseType.OFFLINE to stringResource(R.string.offline),
                CourseType.ONLINE to stringResource(R.string.online),
                CourseType.HYBRID to stringResource(R.string.hybrid)
            )
            val courseTypes = CourseType.entries.map { courseTypeDisplayMap[it] ?: it.name }
            ReusableDropdown(
                label = "Course Type",
                options = courseTypes,
                value = type?.let { courseTypeDisplayMap[it]/* ?: it.toString()*/ } ?: "",
                onOptionSelected = { selectedType ->
                    type = CourseType.entries.find { courseTypeDisplayMap[it] == selectedType }
                    typeError = false
                },
                isError = typeError,
                showRequired = true
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Instructor Name
            InputField(
                value = instructorName,
                onValueChange = { instructorName = it; instructorNameError = false },
                label = "Instructor Name",
                isError = instructorNameError,
                showRequired = true
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Instructor Biography
            InputField(
                value = instructorBio,
                onValueChange = { instructorBio = it; instructorBioError = false },
                label = "Instructor Biography",
                isError = instructorBioError,
                showRequired = true,
                singleLine = false
            )
            Spacer(modifier = Modifier.padding(10.dp))


            // Instructor Image Path
            SelectingImageField(
                imagePath = instructorImg,
                onImagePathChange = { instructorImg = it },
                "Instructor Image"
            )

            // Course Start Date
            SelectingTimeDateField(
                nextLecture = startDate,
                onValueChange = { startDate = it },
                onDelete = { startDate = "" },
                label = "Course Start Date",
                time = false,
            )
            Spacer(modifier = Modifier.padding(10.dp))


            // Course whatsapp Group Link
            InputField(
                value = wGLink,
                onValueChange = { wGLink = it },
                label = "Course whatsapp Group Link"
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Course Details
            InputField(
                value = courseDetails,
                onValueChange = { courseDetails = it },
                label = "Course Details",
                singleLine = false
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Number of literatures
            InputField(
                value = (totalLectures).toString(),
                onValueChange = { totalLectures = it; totalLecturesError= false },  // Now it's String to String
                label = "Total Lectures",
                isError = totalLecturesError,
                showRequired = true,
                keyboardType = "Number"
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Number of literatures finished
            InputField(
                value = (noOfLiteraturesFinished).toString(),
                onValueChange = { noOfLiteraturesFinished = it },
                label = "Number of literatures finished",
                keyboardType = "Number",
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Next Lecture appointment
            SelectingTimeDateField(
                nextLecture = nextLecture,
                onValueChange = { nextLecture = it },
                onDelete = { nextLecture = "" },
                label = "Next Lecture Appointment",
                time = true,
            )

            // Organizer Name
            InputField(
                value = organizerName,
                onValueChange = { organizerName = it },  // Now it's String to String
                label = "Organizer Name",
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Organizer Whatsapp
            InputField(
                value = organizerWhats,
                onValueChange = { organizerWhats = it },
                label = "Organizer Whatsapp",
                keyboardType = "Phone",
                placeholder = "+201012345678"
            )
            Spacer(modifier = Modifier.padding(10.dp))


            // Action Buttons
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

                // Save or Add Button
                val context = LocalContext.current
                Button(
                    onClick = {
                        // Validate required fields
                        titleError = title.isBlank()
                        coursesOfMonthError = coursesOfMonth.isEmpty()
                        branchError = branch.isBlank()
                        categoryError = category.isBlank()
                        typeError = type.toString().isBlank()
                        instructorNameError = instructorName.isBlank()
                        instructorBioError = instructorBio.isBlank()
                        totalLecturesError = totalLectures.isBlank() || totalLectures.toIntOrNull() == null
                        if (titleError || branchError || categoryError || typeError ||
                            instructorNameError || instructorBioError || totalLecturesError || coursesOfMonthError) {
                            // Show error messages
                            Toast.makeText(context,"Please fill all required fields correctly.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val newCourse = Course(
                            id = course?.id ?: 0,
                            branch = branch,
                            coursesOfMonth = coursesOfMonth.map { it.toString() },
                            imagePath = when {
                                selectedImageUri != null -> selectedImageUri.toString()
                                imagePath.isNotEmpty() -> imagePath // Keep existing image path
                                else -> "drawable/anwar_resala_logo"
                            },
                            category = category,
                            title = title,
                            type = type as CourseType,
                            instructor = Instructor(
                                name = instructorName,
                                bio = instructorBio,
                                imagePath = when {
                                    selectedImageUri != null -> selectedImageUri.toString()
                                    instructorImg.isNotEmpty() -> instructorImg // Keep existing instructor image
                                    else -> "drawable/anwar_resala_logo"
                                },
                            ),
                            startDate = startDate,
                            wGLink = wGLink,
                            courseDetails = courseDetails,
                            totalLectures = totalLectures.toIntOrNull() ?: 0,
                            noOfLiteraturesFinished = noOfLiteraturesFinished.toIntOrNull() ?: 0,
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
                    Text(if (courseId == -1) "Add" else "Save")
                }
                // one } for the Row
            }
        }
    }
}




