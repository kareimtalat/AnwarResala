package com.kareimt.anwarresala.data.local.course

import com.kareimt.anwarresala.data.Course
import com.kareimt.anwarresala.data.CourseType

fun CourseEntity.toCourse(): Course = Course(
    id = id,
    branch = branch,
    coursesOfMonth = coursesOfMonth,
    imagePath = imagePath,
    category = category,
    title = title,
    type = CourseType.valueOf(type),
    instructor = Course.Instructor(
        name = instructorName,
        bio = instructorBio,
        imagePath = instructorImagePath
    ),
    startDate = startDate,
    wGLink = wGLink,
    courseDetails = courseDetails,
    totalLectures = totalLectures,
    noOfLiteraturesFinished = noOfLiteraturesFinished,
    nextLecture = nextLecture,
    organizer = Course.Organizer(
            name = organizerName,
            whatsapp = organizerWhatsapp
    ),
    progress = progress
)

fun Course.toEntity(): CourseEntity = CourseEntity(
    id = id,
    branch = branch,
    coursesOfMonth = coursesOfMonth,
    imagePath = imagePath,
    category = category,
    title = title,
    type = type.name,
    instructorName = instructor.name,
    instructorBio = instructor.bio,
    instructorImagePath = instructor.imagePath,
    startDate = startDate,
    wGLink = wGLink,
    courseDetails = courseDetails,
    totalLectures = totalLectures,
    noOfLiteraturesFinished = noOfLiteraturesFinished,
    nextLecture = nextLecture,
    organizerName = organizer.name,
    organizerWhatsapp = organizer.whatsapp,
    progress = progress
)