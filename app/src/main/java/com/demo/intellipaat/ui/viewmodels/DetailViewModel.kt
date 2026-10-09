package com.demo.intellipaat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.intellipaat.data.CourseRepository
import com.demo.intellipaat.domain.Lesson
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: CourseRepository,
    private val courseId: Int
) : ViewModel() {

    val lessons: StateFlow<List<Lesson>> = repository.getLessons(courseId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleLesson(lesson: Lesson){
        viewModelScope.launch {
            repository.toggleLessonCompletion(courseId, lesson.id, lesson.isCompleted)
        }
    }
}