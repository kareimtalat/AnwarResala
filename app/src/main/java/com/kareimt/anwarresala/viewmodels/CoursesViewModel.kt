package com.kareimt.anwarresala.viewmodels

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.CourseType
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CoursesViewModel(context:Context) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    init {
        loadCourses(context =context)
    }

    private fun loadCourses(context: Context) {
        // Example from the 2-2025 table for 10th of Ramadan courses table
        _courses.value = listOf(
            Course(
                id = 1,
                branch = context.getString(R.string.tenth_of_ramadan),
                imageResId = R.drawable.before_marraige,
                category = context.getString(R.string.human_development),
                title = context.getString(R.string.before_marriage_we_should),
                type = CourseType.ONLINE,
                instructor = Course.Instructor(
                    name = context.getString(R.string.dr_layla_saad),
                    bio = context.getString(R.string.dr_layla_saad_bio)
                ),
                startDate = "2025-02-07",
                wGLink = "https://chat.whatsapp.com/CcsREQlt8qL55hkBWzuCoj",
                courseDetails = context.getString(R.string.before_marriage_de),
                totalLectures = 4,
                noOfLiteraturesFinished = 3,
                nextLecture = "2025-02-28 20:00",
                organizer = Course.Organizer(
                    name = context.getString(R.string.fatema_rabee3),
                    whatsapp = "+201093715627"
                )
            ),
            Course(
                id = 2,
                branch = context.getString(R.string.tenth_of_ramadan),
                category = context.getString(R.string.religious),
                title = context.getString(R.string.how_to_raise_your_son),
                type = CourseType.ONLINE,
                instructor = Course.Instructor(
                    name = context.getString(R.string.ms_reham_hamdey),
                    bio = context.getString(R.string.ms_reham_hamdey_bio),
                ),
                startDate = "2025-02-09",
                wGLink = "https://chat.whatsapp.com/GtTsCKUZWOnKa9ChVWCoq4",
                courseDetails = context.getString(R.string.how_to_raise_your_son_de),
                totalLectures = 4,
                noOfLiteraturesFinished = 2,
                nextLecture = "2025-02-23 20:00",
                organizer = Course.Organizer(
                    name = context.getString(R.string.tasnim),
                    whatsapp = "+201032097183"
                ),
            ),
            Course(
                id = 3,
                branch = context.getString(R.string.tenth_of_ramadan),
                imageResId = R.drawable.soft_skills,
                category = context.getString(R.string.human_development),
                title = context.getString(R.string.soft_skills),
                type = CourseType.ONLINE,
                instructor = Course.Instructor(
                    name = context.getString(R.string.ms_ragaa_mohamed),
                    bio = context.getString(R.string.ms_ragaa_mohamed_bio),
                ),
                startDate = "2025-02-15",
                wGLink = "https://chat.whatsapp.com/JNVyhzuzvAV2KqT5PTnoSk",
                courseDetails = context.getString(R.string.soft_skills_de),
                totalLectures = 1,
                noOfLiteraturesFinished = 0,
                nextLecture = "2025-02-15 20:00",
                organizer = Course.Organizer(
                    name = context.getString(R.string.ziko),
                    whatsapp = "+201068484875"
                )
            ),
            Course(
                id = 4,
                branch = context.getString(R.string.tenth_of_ramadan),
                category = context.getString(R.string.human_development),
                title = context.getString(R.string.interview_skills),
                type = CourseType.OFFLINE,
                instructor = Course.Instructor(
                    name = context.getString(R.string.mr_hossam_elsayed),
                    bio = context.getString(R.string.mr_hossam_elsayed_bio),
                    imageResId = R.drawable.hossam_istractor
                ),
                startDate = "2025-02-22",
                wGLink = "https://chat.whatsapp.com/EnYYN2MxPbsAtyiQjqJqWs",
                courseDetails = context.getString(R.string.interview_skills_de),
                totalLectures = 1,
                noOfLiteraturesFinished = 1,
                nextLecture = "2025-02-22 14:30",
                organizer = Course.Organizer(
                    name = context.getString(R.string.kareim_talat),
                    whatsapp = "+201030843508"
                ),
            )
            // TODO: Loading courses data from internet or Local database
        )
    }
}

class CoursesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoursesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoursesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}