package com.kareimt.anwarresala.presentation.screens.beneficiary

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.course.Course
import com.kareimt.anwarresala.data.local.course.toEntity
import com.kareimt.anwarresala.presentation.components.ConfirmationDialog
import com.kareimt.anwarresala.presentation.components.DetailsSection
import com.kareimt.anwarresala.presentation.viewmodels.CoursesViewModel
import com.kareimt.anwarresala.utils.ImageUtils.getImageUri
import com.kareimt.anwarresala.presentation.components.InstructorSection
import com.kareimt.anwarresala.presentation.components.OrganizerSection
import com.kareimt.anwarresala.presentation.components.ProgressSection
import com.kareimt.anwarresala.presentation.viewmodels.VolunteerViewModel

@Composable
fun CourseDetailsScreen(
    course: Course?,
    viewModel: CoursesViewModel,
    onNavigateToEdit: () -> Unit,
    onBack: () -> Boolean,
    volunteerViewModel: VolunteerViewModel
) {
    if (course == null) return

    var showDeleteDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr)  {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()+5.dp
                )
        ) {
            item {
                // صورة الكورس
                val context = LocalContext.current
                val imageUri = remember(course.imagePath ?: "drawable/anwar_resala_logo") {
                    getImageUri(context, course.imagePath)
                }
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Course Image",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))

                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    // Action buttons
                    if ( volunteerViewModel.isLoggedIn &&
                        volunteerViewModel.currentVolunteer?.approved ?: false &&
                        (course.branch == volunteerViewModel.currentVolunteer?.branch || volunteerViewModel.currentVolunteer?.responsibility == stringResource(R.string.activity_officer) ) ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(37.dp, Alignment.CenterHorizontally),
                        ) {
                            // Edit Button
                            IconButton(onClick = { onNavigateToEdit() }) {
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
                        }
                    }

                    // fun for show the Delete Confirmation Dialog
                    if (showDeleteDialog) {
                        ConfirmationDialog(
                            title = stringResource(R.string.delete_course),
                            message = "${stringResource(R.string.are_you_sure_you_want_to_delete_this_course) + " " + course.title}?",
                            onDismiss = { showDeleteDialog = false },
                            onConfirm = {
                                // Only delete course image if it's not the default logo
                                if (course.imagePath != "@drawable/anwar_resala_logo") {
                                    context.deleteFile(course.imagePath)
                                    // TODO: Do like this but for firebase
                                }

                                // Only delete instructor image if it exists and is not the default logo
                                course.instructor.imagePath.let { instructorImage ->
                                    if (instructorImage != "@drawable/anwar_resala_logo") {
                                        context.deleteFile(instructorImage)
                                        // TODO: Do like this but for firebase
                                    }
                                }

                                // Delete course data from database
                                viewModel.deleteCourse(course.toEntity())
                                onBack()
                                Toast.makeText(context, "Course deleted successfully", Toast.LENGTH_SHORT).show()
                                showDeleteDialog = false
                            }
                        )
                    }


                    // معلومات المحاضر
                    InstructorSection(course.instructor)
                    Spacer(modifier = Modifier.height(16.dp))

                    // التفاصيل
                    DetailsSection(course)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Course progress and next lecture
                    ProgressSection(course.progress, course.nextLecture)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Organizer details
                    if (course.organizer?.name!=""){
                        OrganizerSection(course.organizer ?: Course.Organizer("",""))
                    }
                }
            }
        }
    }
}

