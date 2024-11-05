package sultan.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.ui.screens.AddTaskScreen
import sultan.todoapp.ui.screens.MainScreen
import sultan.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationGraph()
                }
            }
        }
    }
    
    @Composable
    fun NavigationGraph(){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "MainScreen") {
            composable("MainScreen") { MainScreen(navController) }
            composable("AddTaskScreen") { AddTaskScreen(navController) }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        MainScreen(navController = rememberNavController())
    }
}