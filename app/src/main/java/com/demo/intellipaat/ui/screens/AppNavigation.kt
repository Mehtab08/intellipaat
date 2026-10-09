package com.demo.intellipaat.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.demo.intellipaat.data.CourseRepository

@Composable
fun AppNavigation(repository: CourseRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(onLoginSuccess = {
                navController.navigate("dashboard") {
                    popUpTo("login") {inclusive = true}
                }
            })
        }
        composable("dashboard") {
            DashboardScreen(
                repository = repository,
                onCourseClick = { courseId ->
                    navController.navigate("detail/$courseId")
                }
            )
        }
        composable(
            route = "detail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            DetailScreen(courseId = courseId, repository = repository)
        }
    }
}