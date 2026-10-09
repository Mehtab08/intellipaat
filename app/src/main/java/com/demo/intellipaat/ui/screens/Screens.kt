package com.demo.intellipaat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.demo.intellipaat.data.CourseRepository
import com.demo.intellipaat.domain.Course
import com.demo.intellipaat.ui.viewmodels.DashboardUiState
import com.demo.intellipaat.ui.viewmodels.DashboardViewModel
import com.demo.intellipaat.ui.viewmodels.DetailViewModel

// --- SCREEN 1: LOGIN ---
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Learning Dashboard Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (email.contains("@") && password.length >= 4) {
                        isLoading = true
                        onLoginSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}

// --- SCREEN 2: COURSE DASHBOARD ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(repository: CourseRepository, onCourseClick: (Int) -> Unit) {
    val viewModel = remember { DashboardViewModel(repository) }
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Courses") }) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val s = state) {
                is DashboardUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is DashboardUiState.Error -> Text(s.message, Modifier.align(Alignment.Center))
                is DashboardUiState.Success -> {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(s.courses) { course ->
                            CourseItem(course = course, onClick = { onCourseClick(course.id) })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseItem(course: Course, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(course.title, style = MaterialTheme.typography.titleMedium)
            Text("Instructor: ${course.instructor}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { course.progress / 100f },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Progress: ${course.progress}% | Lessons: ${course.lessonsCount}")
        }
    }
}

// --- SCREEN 3: COURSE DETAILS ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(courseId: Int, repository: CourseRepository){
    val viewModel = remember(courseId) { DetailViewModel(repository, courseId) }
    val lessons by viewModel.lessons.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Course Details") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(lessons) { lesson ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(lesson.title, style = MaterialTheme.typography.bodyLarge)
                    Checkbox(
                        checked = lesson.isCompleted,
                        onCheckedChange = { viewModel.toggleLesson(lesson) }
                    )
                }
                HorizontalDivider()
            }
        }
    }
}