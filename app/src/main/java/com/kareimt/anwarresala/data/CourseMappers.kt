package com.kareimt.anwarresala.data

import com.kareimt.anwarresala.data.local.CourseEntity

fun CourseEntity.toCourse(): Course = Course(
    id = id,
    branch = branch,
    imageResId = imageResId,
    category = category,
    title = title,
    type = CourseType.valueOf(type),
    instructor = Course.Instructor(
        name = instructorName,
        bio = instructorBio,
        imageResId = instructorImageResId
    ),
    startDate = startDate,
    wGLink = wGLink,
    courseDetails = courseDetails,
    totalLectures = totalLectures,
    noOfLiteraturesFinished = noOfLiteraturesFinished,
    nextLecture = nextLecture,
    organizer = if (organizerName != null && organizerWhatsapp != null)
    {Course.Organizer(
        name = organizerName,
        whatsapp = organizerWhatsapp
    )}else null,
    progress = progress
)