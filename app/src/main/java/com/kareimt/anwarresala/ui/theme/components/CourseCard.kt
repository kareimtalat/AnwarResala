package com.kareimt.anwarresala.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.data.local.course.toEntity
import com.kareimt.anwarresala.ui.theme.screens.ConfirmationDialog
import com.kareimt.anwarresala.utils.ImageUtils.getImageUri
import com.kareimt.anwarresala.viewmodels.CoursesViewModel

// ****** The card of the course
@Composable
fun CourseCard(
    course: Course,
    onItemClick: (Course) -> Unit,
    onEditClick: () -> Unit,
    viewModel: CoursesViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imageUri = remember(course.imagePath) {
        getImageUri(context, course.imagePath)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(course) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // The course image
            AsyncImage(
                model = imageUri,
                contentDescription = "Course Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))

            // Row for Category, Title and Instructor
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Instructor
                Text(
                    text = course.instructor.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(13.dp))

                // Title
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.width(13.dp))

                // Category
                Text(
                    text = course.category,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )

            }


            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                // Course type
                val typeText = when (course.type) {
                    CourseType.ONLINE -> stringResource(R.string.online)
                    CourseType.OFFLINE -> stringResource(R.string.offline)
                    CourseType.HYBRID -> stringResource(R.string.hybrid)
                }
                Text(
                    text = typeText,
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                // The branch
                Text(
                    text ="الفرع: ${course.branch}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(7.dp))

            // التقدم
            ProgressIndicator(progress = course.progress)
            Spacer(Modifier.height(4.dp))

            // إظهار العناصر الاختيارية فقط إذا كانت موجودة
            if (course.progress <1f) {
                if (course.nextLecture !="") {
                    DetailRowForNextLit(icon = Icons.Default.Schedule, text = "المحاضرة القادمة: ${course.nextLecture}")
                }
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(37.dp, Alignment.CenterHorizontally),
            ) {
                // Edit Button
                IconButton(onClick = { onEditClick() }) {
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
    }

    // fun for show the Delete Confirmation Dialog
    if (showDeleteDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.delete_course),
            message = "${stringResource(R.string.are_you_sure_you_want_to_delete_this_course) + " " + course.title}?",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                viewModel.deleteCourse(course.toEntity())
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun DetailRow(icon: ImageVector? = null, text: String, text2: String? = null) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // The detail text
        if (text2 != null) {
            Text(
                text = text2,
                modifier = Modifier
                    .weight(2.5f)
                    //.wrapContentWidth()
                    .padding(end = 15.dp),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        // The label
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DetailRowForNextLit(icon: ImageVector, text: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 3.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // The detail text
            Text(
                text = text,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

}

@Composable
fun ProgressIndicator(progress: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        red = MaterialTheme.colorScheme.primary.red * 0.5f,
                        green = MaterialTheme.colorScheme.primary.green * 0.5f,
                        blue = MaterialTheme.colorScheme.primary.blue * 0.5f
                    )
                )
        ) {
            var progress2 = progress
            if (progress==0f){progress2=progress+0.02f}
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress2)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
                    .clip(RoundedCornerShape(4.dp))
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelMedium
        )
    }
}