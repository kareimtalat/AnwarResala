package com.kareimt.anwarresala.presentation.components

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.course.Course
import com.kareimt.anwarresala.data.local.course.CourseType
import com.kareimt.anwarresala.utils.ImageUtils.getImageUri

@Composable
fun InstructorSection(instructor: Course.Instructor) {
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
            val context = LocalContext.current
            val instructorImgUri = remember(instructor.imagePath) {
                getImageUri(context, instructor.imagePath)
            }
            AsyncImage(
                model = instructorImgUri,
                contentDescription = "Instructor Image",
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
fun DetailsSection(course: Course) {
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
        val type = when (course.type) {
            CourseType.ONLINE -> stringResource(R.string.online)
            CourseType.OFFLINE -> stringResource(R.string.offline)
            CourseType.HYBRID -> stringResource(R.string.hybrid)
        }
        DetailRow(text = "نوع الكورس", text2 = type)
        // Some long description
        if (course.courseDetails.isNotEmpty()) { DetailRow(text = "وصف الكورس", text2 = course.courseDetails) }
        DetailRow(text = "الفرع", text2 =course.branch)
        if (course.startDate.isNotEmpty()) { DetailRow(text = "تاريخ البداية", text2 = course.startDate) }
        DetailRow(text = "عدد محاضرات الكورس", text2 = course.totalLectures.toString())
        DetailRow(text = "عدد المحاضرات المنتهية", text2 = course.noOfLiteraturesFinished.toString())
        if (course.wGLink.isNotEmpty()) { WGSection(course.wGLink) }
    }
}

// The WhatsApp link to the course group
@Composable
fun WGSection(
    wGLink: String
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                try {
                    // Ensure the link is properly formatted
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = if (wGLink.startsWith("http")) {
                            wGLink.toUri()
                        } else {
                            "https://$wGLink".toUri()
                        }
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Couldn't open WhatsApp", Toast.LENGTH_SHORT).show()
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
fun ProgressSection(progress: Float, nextLecture: String?=null) {
    Column {
        ProgressIndicator(progress = progress)
        if (progress<1f)/*Mean course not finished yet*/ {
            if (nextLecture!=""){
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
fun OrganizerSection(organizer: Course.Organizer) {
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
        if (organizer.whatsapp!="") {
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
}