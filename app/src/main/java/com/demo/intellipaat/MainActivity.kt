package com.demo.intellipaat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.demo.intellipaat.data.CourseRepository
import com.demo.intellipaat.data.local.AppDatabase
import com.demo.intellipaat.ui.screens.AppNavigation
import com.demo.intellipaat.ui.theme.IntellipaatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "learning_db"
        ).build()

        val repository = CourseRepository(db.courseDao())

        setContent {
            AppNavigation(repository = repository)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IntellipaatTheme {
        Greeting("Android")
    }
}