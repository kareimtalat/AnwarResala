package com.kareimt.anwarresala.presentation.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.course.Course
import com.kareimt.anwarresala.utils.ImageUtils
import com.kareimt.anwarresala.presentation.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModel
import java.text.DateFormatSymbols
import java.time.Month
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectingTimeDateField(
    nextLecture: String,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit,
    label: String,
    time : Boolean
){
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var tempDate by remember { mutableStateOf("") }
    Column {
        Row (
            modifier = Modifier.widthIn(max=300.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (nextLecture.isNotEmpty()) {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(0.3f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear Next Lecture"
                    )
                }
            }

            InputField(
                value = nextLecture,
                onValueChange = onValueChange,
                label = label,
                enabled = false,
                modifier = Modifier
                    .clickable { showDatePicker = true }
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .weight(1f),
                textStyle = LocalTextStyle.current.copy(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary
                ),
            )
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Calendar.getInstance().apply { timeInMillis = millis }
                            tempDate = "${date.get(Calendar.DAY_OF_MONTH)}/" +
                                    "${date.get(Calendar.MONTH) + 1}/" +
                                    "${date.get(Calendar.YEAR)}"
                            showTimePicker = true // Show time picker after date selection
                        }
                        showDatePicker = false
                        onValueChange (tempDate)
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker && time) {
            val timePickerState = rememberTimePickerState()
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                title = { Text(stringResource(R.string.select_time)) },
                text = {
                    TimePicker(
                        state = timePickerState
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                        }
                        val time = String.format(
                            Locale.US,
                            "%02d:%02d",
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE)
                        )
                        onValueChange ("$tempDate $time")
                        showTimePicker = false
                    }) {
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
    Spacer(modifier = Modifier.padding(10.dp))
}

// Course Image Field
@Composable
fun SelectingImageField(
    imagePath: String,
    onImagePathChange: (String) -> Unit,
    fieldName: String
) {
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
            onImagePathChange(filename)
            imageUri = uri
        }
    }
    // Image selection button and preview
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 37.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = fieldName,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Image preview
        if (imageUri != null || imagePath.isNotEmpty()) {
            AsyncImage(
                model = imageUri ?: ImageUtils.getImageUri(LocalContext.current, imagePath),
                contentDescription = "Selected image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { imagePicker.launch("image/*") }
        ) {
            Text(stringResource(R.string.select_image))
        }

        // Delete button
        if (imageUri != null || imagePath.isNotEmpty()) {
            Button(
                onClick = {
                    if (imagePath != "@drawable/anwar_resala_logo") {
                        context.deleteFile(imagePath)
                    }
                    imageUri = null
                    onImagePathChange("")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.delete_image))
            }
        }
    }
    Spacer(modifier = Modifier.padding(10.dp))
}

// CourseOFMonths Field
@Composable
fun CoursesOfMonthField(
    course: Course? = null,
    coursesOfMonth: (List<String>) -> Unit,
    coursesOfMonthError: Boolean,
    onErrorChange: (Boolean) -> Unit,
){
    var showMonthPickerDialog by remember { mutableStateOf(false) }
    var selectedMonths by remember {
        mutableStateOf(course?.coursesOfMonth ?: emptyList())
    }
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
                    text = stringResource(R.string.required_field),
                    color = Color.Red,
                    modifier = Modifier.padding( start = 40.dp )
                )
            }

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.course_of_months))
                    append(" ")
                    selectedMonths.forEachIndexed { index, month ->
                        withStyle(style = SpanStyle(color = Color(0xFFB0E0E6))) { // Light Sky Blue
                            append("${Month.valueOf(month.split(" ")[0].uppercase())} ${month.split(" ")[1]}")
                        }
                        if (index != selectedMonths.lastIndex) append(", ")
                    }
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
//                    .layoutDirection(LayoutDirection.Rtl)
                ,
            )

            Button(onClick = { showMonthPickerDialog = true }) {
                Text(stringResource(R.string.select_months))
            }
        }
        if (coursesOfMonthError) {
            Text(
                text = stringResource(R.string.please_select_at_least_one_month),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
    if (showMonthPickerDialog) {
        AlertDialog(
            onDismissRequest = { showMonthPickerDialog = false },
            title = { Text(stringResource(R.string.select_course_months)) },
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Previous Year")
                        }
                        Text(currentDate.get(Calendar.YEAR).toString())
                        IconButton(onClick = {
                            currentDate = Calendar.getInstance().apply {
                                timeInMillis = currentDate.timeInMillis
                                add(Calendar.YEAR, 1)
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next Year")
                        }
                    }

                    // Month grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(12) { index ->
                            val month = Month.entries[index]
//                            val arabicMonths = DateFormatSymbols(Locale("ar")).months
//                            val monthNameArabic = arabicMonths[month.ordinal]
//                            val monthYear = "$monthNameArabic ${currentDate.get(Calendar.YEAR)}"
                            val monthYear = "${month.name.lowercase().capitalize(Locale.ROOT)} ${currentDate.get(Calendar.YEAR)}"
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
                    onErrorChange(selectedMonths.isEmpty())
                    coursesOfMonth(selectedMonths)
                    showMonthPickerDialog = false
                }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showMonthPickerDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    Spacer(modifier = Modifier.padding(10.dp))
}


// Branch Field
@Composable
fun BranchField(
    viewModel: CoursesViewModel,
    branch: String,
    onOptionSelected: (String) -> Unit,
    branchError: Boolean,
    volunteerViewModel: VolunteerViewModel,
){
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
            IconButton(onClick = { showAddBranchDialog = true},
                modifier = Modifier
                    .weight(0.25f)
                    .padding(top = 22.dp)
            ) {
                Icon(Icons.Default.Add, "Add Branch")
            }

            ReusableDropdown(
                modifier = Modifier.weight(1f),
                label = "Branch",
                options = branches.map { it.branch },
                value = branch,
                onOptionSelected = onOptionSelected,
                isError = branchError,
                showRequired = true,
                textRPadding = 0,
            )
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
                            viewModel.addBranch(newBranchName)
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
    confirmText: String = stringResource(R.string.confirm),
    dismissText: String = stringResource(R.string.cancel)
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