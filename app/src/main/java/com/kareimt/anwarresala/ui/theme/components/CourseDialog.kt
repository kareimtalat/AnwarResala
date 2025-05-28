package com.kareimt.anwarresala.ui.theme.components

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import kotlin.text.category
import kotlin.toString
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kareimt.anwarresala.data.Course.Instructor
import com.kareimt.anwarresala.data.Course.Organizer
import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.data.local.BranchEntity
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddEditCourseDialog(
    course: Course?,
    viewModel: CoursesViewModel,
    onDismiss: () -> Unit,
    onConfirm: (Course) -> Unit
) {
    var category by remember { mutableStateOf(course?.category ?: "") }
    var title by remember { mutableStateOf(course?.title ?: "") }
    var branch by remember { mutableStateOf(course?.branch ?: "") }
    val branches: State<List<BranchEntity>> = viewModel.branches.collectAsState(initial = emptyList())
    var type by remember { mutableStateOf(course?.type ?: CourseType.ONLINE) }
    var instructorName by remember { mutableStateOf(course?.instructor?.name ?: "") }  // Fix this
    var instructorBio by remember { mutableStateOf(course?.instructor?.bio ?: "") }  // Fix this
    var instructorImg by remember { mutableStateOf(course?.instructor?.imagePath?.toString() ?: "drawable/anwar_resala_logo") }  // Change to String
    var startDate by remember { mutableStateOf(course?.startDate ?: "") }
    var wGLink by remember { mutableStateOf(course?.wGLink ?: "") }
    var courseDetails by remember { mutableStateOf(course?.courseDetails ?: "") }
    var totalLectures by remember { mutableStateOf(course?.totalLectures?.toString() ?: "") }  // Change to String
    var noOfLiteraturesFinished by remember { mutableStateOf(course?.noOfLiteraturesFinished?.toString() ?: "") }  // Change to String
    var nextLecture by remember { mutableStateOf(course?.nextLecture ?: "") }
    var organizerName by remember { mutableStateOf(course?.organizer?.name ?: "") }
    var organizerWhats by remember { mutableStateOf(course?.organizer?.whatsapp ?: "") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf(course?.imagePath ?: "drawable/anwar_resala_logo") }
    // TODO: ... other variables remain the same ...

    // Create new course with updated image handling
//    val newCourse = Course(
//        id = course?.id ?: 0,
//        title = title,
//        branch = branch,
//        category = category,
//        type = type,
//        imagePath = when {
//            selectedImageUri != null -> selectedImageUri.toString()
//            else -> imagePath
//        },
//        instructor = Instructor(
//            name = instructorName,
//            bio = instructorBio,
//            imageResId = instructorImg.toIntOrNull() ?: R.drawable.anwar_resala_logo
//        ),
//        // TODO: ... other properties remain the same ...
//    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // ... other code ...

                OutlinedTextField(
                    value = imagePath,
                    onValueChange = { imagePath = it },  // Now it's String to String
                    label = { Text("Image Resource ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = totalLectures,
                    onValueChange = { totalLectures = it },  // Now it's String to String
                    label = { Text("Total Lectures") },
                    modifier = Modifier.fillMaxWidth()
                )

                // When creating the Course object, convert types appropriately
                Button(
                    onClick = {
                        val newCourse = Course(
                            id = course?.id ?: 0,
                            title = title,
                            branch = branch,
                            category = category,
                            type = type,
                            // TODO: Handle imagePath properly
                            imagePath = imagePath,
                            instructor = Instructor(  // Create proper Instructor object
                                name = instructorName,
                                bio = instructorBio,
                                // TODO: Handle imagePath properly
                                imagePath = instructorImg
                            ),
                            startDate = startDate,
                            wGLink = wGLink,
                            courseDetails = courseDetails,
                            totalLectures = totalLectures.toIntOrNull() ?: 0,
                            noOfLiteraturesFinished = noOfLiteraturesFinished.toIntOrNull() ?: 0,
                            nextLecture = nextLecture,
                            organizer = Organizer(  // Create proper Organizer object
                                name = organizerName,
                                whatsapp = organizerWhats
                            )
                        )
                        onConfirm(newCourse)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(if (course == null) "Add" else "Save")
                }
            }
        }
    }
}