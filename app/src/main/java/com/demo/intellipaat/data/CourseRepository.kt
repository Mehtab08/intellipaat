package com.demo.intellipaat.data

import com.demo.intellipaat.data.local.CourseDao
import com.demo.intellipaat.data.local.CourseEntity
import com.demo.intellipaat.data.local.LessonEntity
import com.demo.intellipaat.domain.Course
import com.demo.intellipaat.domain.Lesson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

class CourseRepository(private val courseDao: CourseDao) {

    // Emits cached Room DB data continuously
    fun getCourses(): Flow<List<Course>> {
        return courseDao.getAllCourses().map { list ->
            list.map { Course(it.id, it.title, it.instructor, it.progress, it.lessonsCount) }
        }
    }

    fun getLessons(courseId: Int): Flow<List<Lesson>> {
        return courseDao.getLessonsForCourse(courseId).map { list ->
            list.map { Lesson(it.id, it.courseId, it.title, it.isCompleted) }
        }
    }

    // Mock initial API seed into DB
    suspend fun seedInitialDataIfEmpty() {
        val initialCourses = listOf(
            CourseEntity(1, "Python Programming", "John Smith", 50, 2),
            CourseEntity(2, "Generative AI", "Sarah Williams", 0, 2),
            CourseEntity(3, "Full Stack Development", "David Brown", 0, 2)
        )
        val initialLessons = listOf(
            LessonEntity(101, 1, "Introduction", true),
            LessonEntity(102, 1, "Variables & Data Types", false),
            LessonEntity(201, 2, "AI Basics", false),
            LessonEntity(202, 2, "Prompt Engineering", false),
            LessonEntity(301, 3, "HTML & CSS", false),
            LessonEntity(302, 3, "Node.js Basics", false)
        )
        courseDao.insertCourses(initialCourses)
        courseDao.insertLessons(initialLessons)
    }

    suspend fun toggleLessonCompletion(courseId: Int, lessonId: Int, currentStatus: Boolean){
        val newStatus = !currentStatus
        courseDao.updateLessonStatus(courseId, newStatus)

        val lessons = courseDao.getLessonsListSync(courseId)
        val completedCount = lessons.count{ it.isCompleted }
        val newProgress = if (lessons.isNotEmpty()) {
            ((completedCount.toDouble() / lessons.size) * 100).toInt()
        } else 0

        courseDao.updateCourseProgress(courseId, newProgress)
    }
}