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

fun Course.toEntity(): CourseEntity = CourseEntity(
    id = id,
    branch = branch,
    imageResId = imageResId,
    category = category,
    title = title,
    type = type.name,
    instructorName = instructor.name,
    instructorBio = instructor.bio,
    instructorImageResId = instructor.imageResId,
    startDate = startDate,
    wGLink = wGLink,
    courseDetails = courseDetails,
    totalLectures = totalLectures,
    noOfLiteraturesFinished = noOfLiteraturesFinished,
    nextLecture = nextLecture,
    organizerName = organizer?.name,
    organizerWhatsapp = organizer?.whatsapp,
    progress = progress
)