package com.kareimt.anwarresala.ui.theme.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import com.kareimt.anwarresala.ui.theme.components.DetailRow
import com.kareimt.anwarresala.ui.theme.components.DetailRowForNextLit
import com.kareimt.anwarresala.ui.theme.components.ProgressIndicator
import coil.compose.AsyncImage
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.layout.ContentScale
import com.kareimt.anwarresala.data.toEntity
import com.kareimt.anwarresala.ui.theme.components.AddEditCourseDialog
import com.kareimt.anwarresala.ui.theme.components.ConfirmationDialog
import com.kareimt.anwarresala.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.viewmodels.CoursesViewModelFactory
import kotlin.getValue


class CourseDetailsActivity : ComponentActivity() {
    private val viewModel: CoursesViewModel by viewModels { CoursesViewModelFactory(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val course =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra<Course>("course", Course::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Course>("course")
            }

        setContent {
            AnwarResalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    course?.let {
                        CourseDetailsScreen(
                            course = it,
                            viewModel = viewModel,
                            { navController.navigate(Routes.addEditCourse(courseId)) },
                            { navController.navigateUp() }
                        ) { navController.navigateUp() }
                    } ?: run {
                        Text(
                            text = "Course details not available",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

// The last Composable here, which will already appear
@Composable
fun CourseDetailsScreen(
    course: Course?,
    viewModel: CoursesViewModel,
    onNavigateToEdit: () -> Unit,
    onBack: () -> Boolean,
    onDeleteCourse: () -> Boolean
) {
    if (course == null) return

    LazyColumn(modifier = Modifier
        .padding(16.dp)
    ) {
        item {
            // صورة الكورس
            // Anwar logo as a placeholder
            if (course.imagePath.startsWith("drawable/")) {
                val resourceId = LocalContext.current.resources.getIdentifier(
                    course.imagePath.removePrefix("drawable/"),
                    "drawable",
                    LocalContext.current.packageName
                )
                Image(
                    painter = painterResource(resourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
                // The image which the user chosen
            } else {
                AsyncImage(
                    model = course.imagePath,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(Modifier.height(16.dp))

            // The Edit & Delete buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterHorizontally),
            ) {
                var showDeleteDialog by remember { mutableStateOf(false) }
                var showEditDialog by remember { mutableStateOf(false) }

                // Edit Button
                IconButton(onClick = {showEditDialog=true}) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                // Delete Button
                IconButton(onClick = {showDeleteDialog=true}) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                // fun for show the Edit Dialog
                if (showEditDialog) {
                    AddEditCourseDialog(
                        course = course,
                        viewModel = viewModel,
                        onDismiss = { showEditDialog = false },
                        onConfirm = { updatedCourse ->
                            viewModel.updateCourse(updatedCourse.toEntity())
                            showEditDialog = false
                        }
                    )
                }

                // fun for show the Delete Confirmation Dialog
                if (showDeleteDialog) {
                    ConfirmationDialog(
                        title = stringResource(R.string.delete_course),
                        message = "${stringResource(R.string.are_you_sure_you_want_to_delete_this_course)+" "+course.title}?",
                        onDismiss = { showDeleteDialog = false },
                        onConfirm = {
                            viewModel.deleteCourse(course.toEntity())
                            showDeleteDialog = false
                        }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            // Course details in general
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                // معلومات المحاضر
                InstructorSection(course.instructor)
                Spacer(modifier = Modifier.height(16.dp))

                // التفاصيل
                DetailsSection(course)
                Spacer(modifier = Modifier.height(16.dp))

                // التقدم
                ProgressSection(course.progress, course.nextLecture)

                Spacer(modifier = Modifier.height(16.dp))
                // التواصل
                course.organizer?.let { organizer ->
                    OrganizerSection(organizer)
                }
            }
        }
    }
}


@Composable
private fun InstructorSection(instructor: Course.Instructor) {
    Column {
        Text(
            text = "المحاضر",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column (horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ){
                // Instructor name
                Text(
                    text = instructor.name,
                    style = MaterialTheme.typography.titleSmall
                )
                // Instructor cv/bio
                val darkerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                    red = MaterialTheme.colorScheme.onSurfaceVariant.red * 0.8f,
                    green = MaterialTheme.colorScheme.onSurfaceVariant.green * 0.8f,
                    blue = MaterialTheme.colorScheme.onSurfaceVariant.blue * 0.8f
                )
                Text(
                    text = instructor.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = darkerColor,
                    textAlign = TextAlign.End
                )
            }
            Spacer(Modifier.width(13.dp))

            // Instructor image
            AsyncImage(
                model = if (instructor.imagePath.startsWith("drawable/")){
                    R.drawable.anwar_resala_logo
                }else{
                instructor.imagePath
            },
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// The details of the course
@Composable
private fun DetailsSection(course: Course) {
    Column (
        Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = "تفاصيل الكورس",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        DetailRow(text = LocalContext.current.getString(R.string.the_title), text2 = course.title)
        DetailRow(text = "التصنيف", text2 = course.category)
        DetailRow(text = "نوع الكورس", text2 = course.type.toString())
        // Some long description
        if (course.courseDetails!="") {
            DetailRow(text = "وصف الكورس", text2 = course.courseDetails)
        }
        DetailRow(text = "الفرع", text2 =course.branch)
        DetailRow(text = "تاريخ البداية", text2 = course.startDate)
        DetailRow(text = "عدد محاضرات الكورس", text2 = course.totalLectures.toString())
        DetailRow(text = "عدد المحاضرات المنتهية", text2 = course.noOfLiteraturesFinished.toString())

        if (course.wGLink!="") {
            WGSection(course.wGLink.toString())
        }
    }
}

// The WhatsApp link to the course group
@Composable
private fun WGSection(wGLink: String) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, wGLink.toUri())
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // Handel error - you can show a toast
                    Toast.makeText(
                        context,
                        "Unable to open Whatsapp group link",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "رابط جروب الكورس")
        Spacer(modifier = Modifier.width(1.dp))
        Image(
            painter = painterResource(R.drawable.whatsapp),
            contentDescription = null,
            modifier = Modifier
                .height(32.dp)
                .padding(bottom = 7.dp)
        )
    }
}

// The progress of the course and the next lecture appointment
@Composable
private fun ProgressSection(progress: Float, nextLecture: String?=null) {
    Column {
        ProgressIndicator(progress = progress)
        if (progress<1f) {
            if (nextLecture!="") {
                DetailRowForNextLit(
                    icon = Icons.Default.Schedule,
                    text = "المحاضرة القادمة: $nextLecture"
                )
            }
        }
    }
}

// The details of the organization member
@Composable
private fun OrganizerSection(organizer: Course.Organizer) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "مسؤول التنظيم",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        val icon= Icons.Default.Person
        val text= organizer.name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp), // Add vertical padding for more spacing
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(17.dp))
            // Organizer name
            Text(
                text = text,
                //textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge // Use a larger text style
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp) // Increase the icon size
                    .padding(end = 12.dp, start = 5.dp), // Add more padding
                tint = MaterialTheme.colorScheme.primary
            )


        }
        val context= LocalContext.current

        // Organizer whats No.
         Row(
            modifier = Modifier
                //.padding(top = 3.dp)
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://wa.me/${organizer.whatsapp}".toUri()
                    }
                    context.startActivity(intent)
                },
            horizontalArrangement = Arrangement.Center,
        ) {
             Spacer(Modifier.width(8.dp))
             Text(text = organizer.whatsapp)
             Image(
                 painter = painterResource(R.drawable.whatsapp),
                 contentDescription = null,
                 modifier = Modifier
                     .height(32.dp)
                     .padding(bottom = 7.dp)
                 )
        }
    }
}
