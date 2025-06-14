package com.kareimt.anwarresala.data

import com.kareimt.anwarresala.data.local.course.CourseEntity

fun CourseEntity.toCourse(): Course = Course(
    id = id,
    branch = branch,
    imagePath = imagePath?: "drawable/anwar_resala_logo",
    category = category,
    title = title,
    type = CourseType.valueOf(type),
    instructor = Course.Instructor(
        name = instructorName,
        bio = instructorBio,
        imagePath = instructorImagePath?: "drawable/anwar_resala_logo"
    ),
    startDate = startDate,
    wGLink = wGLink,
    courseDetails = courseDetails,
    totalLectures = totalLectures,
    noOfLiteraturesFinished = noOfLiteraturesFinished?: 0,
    nextLecture = nextLecture,
    organizer = if (organizerName != null && organizerWhatsapp != null)
    {Course.Organizer(
        name = organizerName,
        whatsapp = organizerWhatsapp
    )} else null,
    progress = progress
)

fun Course.toEntity(): CourseEntity = CourseEntity(
    id = id,
    branch = branch,
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
    organizerName = organizer?.name,
    organizerWhatsapp = organizer?.whatsapp,
    progress = progress
)