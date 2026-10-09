package com.demo.intellipaat.data.local

import androidx.room.*
import androidx.room.Insert
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val instructor: String,
    val progress: Int,
    val lessonsCount: Int
)

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: Int,
    val courseId: Int,
    val title: String,
    val isCompleted: Boolean
)

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Query("SELECT * FROM lessons WHERE courseId = :courseId")
    fun getLessonsForCourse(courseId: Int): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE courseId = :courseId")
    suspend fun getLessonsListSync(courseId: Int): List<LessonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("UPDATE lessons SET isCompleted = :isCompleted WHERE id = :lessonId")
    suspend fun updateLessonStatus(lessonId: Int, isCompleted: Boolean)

    @Query("UPDATE courses SET progress = :progress WHERE id = :courseId")
    suspend fun updateCourseProgress(courseId: Int, progress: Int)


}

@Database(entities = [CourseEntity::class, LessonEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
