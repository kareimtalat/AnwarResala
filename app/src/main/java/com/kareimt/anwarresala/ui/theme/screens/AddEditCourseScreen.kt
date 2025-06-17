package com.kareimt.anwarresala.ui.theme.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.Course.Instructor
import com.kareimt.anwarresala.data.Course.Organizer
import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.data.local.course.toCourse
import com.kareimt.anwarresala.data.local.course.toEntity
import com.kareimt.anwarresala.ui.theme.components.InputField
import com.kareimt.anwarresala.ui.theme.components.ReusableDropdown
import com.kareimt.anwarresala.utils.ImageUtils
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import java.time.Month
import java.util.Calendar

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
            val branches by viewModel.branches.collectAsState()
            var showAddBranchDialog by remember { mutableStateOf(false) }
            var newBranchName by remember { mutableStateOf("") }
            var newBranchError by remember { mutableStateOf(false) } // For the dialog
            Column {
                Row (
                    modifier = Modifier.widthIn(max = 300.dp),
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
                        showRequired = true,
                        textRPadding = 0,
                    )
                    IconButton(onClick = { showAddBranchDialog = true},
                        modifier = Modifier
                            .weight(0.25f)
                            .padding(top = 22.dp)
                    ) {
                        Icon(Icons.Default.Add, "Add Branch")
                    }
                }

                // Add Branch Dialog
                if (showAddBranchDialog){
                    AlertDialog(
                        onDismissRequest = {
                            showAddBranchDialog = false
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
                                    showAddBranchDialog = false
                                    newBranchName = ""
                                }) {
                                Text("Add")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showAddBranchDialog = false
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

            // CoursesOfMonth
            var showMonthPickerDialog by remember { mutableStateOf(false) }
            var selectedMonths by remember {
                mutableStateOf<List<String>>(
                    (if(course?.coursesOfMonth != null && course.coursesOfMonth.isNotEmpty()){
                        course.coursesOfMonth
                    } else emptyList<String>())
                ) }
            var currentDate by remember { mutableStateOf(Calendar.getInstance()) }
            Column{
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = "* Required Field",
                            color = Color.Red,
                            modifier = Modifier.padding( start = 40.dp )
                        )
                    }
                    Text(
                        text = "Selected Months: ${
                            selectedMonths.joinToString(", ") {
                                "${Month.valueOf(it.split(" ")[0].uppercase())} ${it.split(" ")[1]}"
                            }
                        }",
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Button(onClick = { showMonthPickerDialog = true }) {
                        Text("Select Months")
                    }
                }
                if (coursesOfMonthError) {
                    Text(
                        text = "Please select at least one month.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp)
                            .fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            if (showMonthPickerDialog) {
                AlertDialog(
                    onDismissRequest = { showMonthPickerDialog = false },
                    title = { Text("Select Course Months") },
                    text = {
                        Column {
                            // Year-Month navigation
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = {
                                    currentDate = Calendar.getInstance().apply {
                                        timeInMillis = currentDate.timeInMillis
                                        add(Calendar.YEAR, -1)
                                    }
                                }) {
                                    Icon(Icons.Default.ArrowBack, "Previous Year")
                                }
                                Text(currentDate.get(Calendar.YEAR).toString())
                                IconButton(onClick = {
                                    currentDate = Calendar.getInstance().apply {
                                        timeInMillis = currentDate.timeInMillis
                                        add(Calendar.YEAR, 1)
                                    }
                                }) {
                                    Icon(Icons.Default.ArrowForward, "Next Year")
                                }
                            }

                            // Month grid
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.height(200.dp)
                            ) {
                                items(12) { index ->
                                    val month = Month.entries[index]
                                    val monthYear = "${month.name.lowercase().capitalize()} ${currentDate.get(Calendar.YEAR)}"
                                    val isSelected = selectedMonths.contains(monthYear)

                                    OutlinedButton(
                                        onClick = {
                                            selectedMonths = if (isSelected) {
                                                selectedMonths - monthYear
                                            } else {
                                                selectedMonths + monthYear
                                            }
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (isSelected) {
                                                MaterialTheme.colorScheme.primaryContainer
                                            } else {
                                                MaterialTheme.colorScheme.surface
                                            }
                                        ),
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text(month.name.substring(0, 3))
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            coursesOfMonth = selectedMonths
                            coursesOfMonthError = selectedMonths.isEmpty()
                            showMonthPickerDialog = false
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showMonthPickerDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))

            // Course Image
            val context = LocalContext.current
            var imageUri by remember {
                mutableStateOf<Uri?>(
                    if (imagePath.isNotEmpty()) {
                        ImageUtils.getImageUri(context, imagePath)
                    } else null
                )
            }

            val imagePicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    // Copy the image to app's internal storage
                    val timestamp = System.currentTimeMillis()
                    val filename = "course_image_$timestamp.jpg"
                    val inputStream = context.contentResolver.openInputStream(it)
                    context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                        inputStream?.copyTo(outputStream)
                    }
                    imagePath = filename
                    imageUri = uri
                }
            }
            // Image selection button and preview
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Course Image",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                // Image preview
                if (imageUri != null || imagePath.isNotEmpty()) {
                    AsyncImage(
                        model = imageUri ?: imagePath,
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = { imagePicker.launch("image/*") }
                ) {
                    Text("Select Image")
                }

                // Delete button
                if (imageUri != null || imagePath.isNotEmpty()) {
                    Button(
                        onClick = {
                            imageUri = null
                            imagePath = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete Image")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))

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
                showRequired = true
            )
            Spacer(modifier = Modifier.padding(10.dp))


            // Instructor Image Path
            var instructorImgUri by remember {
                mutableStateOf<Uri?>(
                    if (instructorImg.isNotEmpty()) {
                        ImageUtils.getImageUri(context, instructorImg)
                    } else null
                )
            }
            LaunchedEffect(course) {
                course?.let {
                    if (it.imagePath.isNotEmpty()) {
                        imageUri = ImageUtils.getImageUri(context, it.imagePath)
                    }
                    if (it.instructor.imagePath.isNotEmpty()) {
                        instructorImgUri = ImageUtils.getImageUri(context, it.instructor.imagePath)
                    }
                }
            }

            val instructorImgPicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    // Copy the image to app's internal storage
                    val timestamp = System.currentTimeMillis()
                    val filename = "instructor_img_$timestamp.jpg"
                    val inputStream = context.contentResolver.openInputStream(it)
                    context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                        inputStream?.copyTo(outputStream)
                    }
                    instructorImg = filename
                    instructorImgUri = uri
                }
            }
            // (Image selection button) and (preview)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Instructor Image",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Image preview
                if (instructorImgUri != null || instructorImg.isNotEmpty()) {
                    AsyncImage(
                        model = instructorImgUri ?: instructorImg,
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Button(
                    onClick = { instructorImgPicker.launch("image/*") }
                ) {
                    Text("Select Image")
                }

                // Delete button
                if (instructorImgUri != null || instructorImg.isNotEmpty()) {
                    Button(
                        onClick = {
                            instructorImgUri = null
                            instructorImg = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete Image")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))

            // Course Start Date
            InputField(
                value = startDate,
                onValueChange = { startDate = it },
                label = "Course Start Date",
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
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
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
                onValueChange = { noOfLiteraturesFinished = it },  // Now it's String to String
                label = "Number of literatures finished",
                keyboardType = "Number"
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Next Lecture appointment
            InputField(
                value = nextLecture,
                onValueChange = { nextLecture = it },
                label = "Next Lecture appointment",
            )
            Spacer(modifier = Modifier.padding(10.dp))

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
                onValueChange = { organizerWhats = it },  // Now it's String to String
                label = "Organizer Whatsapp",
                keyboardType = "Phone"
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // TODO: When creating the Course object, convert types appropriately
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