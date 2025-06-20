package com.kareimt.anwarresala.ui.theme.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.ui.theme.AnwarResalaTheme
import com.kareimt.anwarresala.ui.theme.components.DetailRow
import com.kareimt.anwarresala.ui.theme.components.DetailRowForNextLit
import com.kareimt.anwarresala.ui.theme.components.ProgressIndicator
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.layout.ContentScale


class CourseDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // Perform any heavy initialization or data loading here


            val course =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra<Course>("course", Course::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<Course>("course")
                }
            if (course == null) {
                println("Course is null")
                finish()
                return@launch
            }

            // Once data is ready, set the content
            setContent {
                AnwarResalaTheme {
                    CourseDetailsScreen(course = course)
                }
            }
        }
    }
}

@Composable
fun CourseDetailsScreen(course: Course?) {
    if (course == null) return

    LazyColumn(modifier = Modifier
        .padding(16.dp)
    ) {
        item {
            // صورة الكورس
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
            if (instructor.imagePath.startsWith("drawable/")) {
                Image(
                    painter = painterResource(R.drawable.anwar_resala_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = instructor.imagePath,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
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
        course.courseDetails?.let { DetailRow(text = "وصف الكورس", text2 = it) }
        DetailRow(text = "الفرع", text2 =course.branch)
        DetailRow(text = "تاريخ البداية", text2 = course.startDate)
        DetailRow(text = "عدد محاضرات الكورس", text2 = course.totalLectures.toString())
        DetailRow(text = "عدد المحاضرات المنتهية", text2 = course.noOfLiteraturesFinished.toString())
        course.wGLink?.let { WGSection(it) }
    }
}

// The WhatsApp link to the course group
@Composable
private fun WGSection(
    wGLink: String
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = wGLink.toUri()
                }
                context.startActivity(intent)
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
            nextLecture?.let {
                DetailRowForNextLit(
                    icon = Icons.Default.Schedule,
                    text = "المحاضرة القادمة: $it"
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
