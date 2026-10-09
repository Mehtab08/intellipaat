package com.demo.intellipaat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.intellipaat.data.CourseRepository
import com.demo.intellipaat.domain.Course
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(val courses: List<Course>) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

class DashboardViewModel(private val repository: CourseRepository) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = repository.getCourses()
        .map { courses ->
            if(courses.isEmpty()) DashboardUiState.Error("No course available")
            else DashboardUiState.Success(courses)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState.Loading
        )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }
}