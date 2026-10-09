package com.demo.intellipaat.domain

data class Course(
    val id: Int,
    val title: String,
    val instructor: String,
    val progress: Int,
    val lessonsCount: Int,
)

data class Lesson(
    val id: Int,
    val courseId: Int,
    val title: String,
    val isCompleted: Boolean
)
